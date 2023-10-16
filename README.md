# Outbox Pattern

## Overview

> As part of their business logic, microservices often do not only have to update their own local data store, but they
> also need to notify other services about data changes that happened. The outbox pattern describes an approach for
> letting services execute these two tasks in a safe and consistent manner; it provides source services with instant
> "read your own writes" semantics, while offering reliable, eventually consistent data exchange across service boundaries.
> -- **Gunnar Morling**


In this demo we will describe how to implement the `Create Order` saga. Let’s begin by looking at how to coordinate sagas.

```shell
docker-compose -f ./docker-compose.yml  -f ./docker-compose-dbs.yml -f ./docker-compose-wavefront.yml -f ./docker-compose-dood.yml up
```

```
order=order-service --spring.datasource.driver-class-name=org.postgresql.Driver --spring.datasource.username=postgresuser --spring.datasource.url="jdbc:postgresql://order-db:5432/orderdb?currentSchema=inventory" --spring.datasource.password=postgrespw  --server.port=21907
```

```shell
cdc-outbox-shipment = cdc-debezium --cdc.name=mycdc --cdc.flattening.enabled=true --cdc.connector=postgres --cdc.config.database.user=postgresuser --cdc.config.database.password=postgrespw --cdc.config.database.dbname=orderdb --cdc.config.database.hostname=order-db --cdc.config.database.port=5432 --cdc.stream.header.offset=true --cdc.config.database.server.name=my-app-connector --cdc.config.tombstones.on.delete=false --cdc.config.table.include.list=inventory.outbox_event | shipment --spring.datasource.driver-class-name=org.postgresql.Driver --spring.datasource.url="jdbc:postgresql://shipment-db:5432/shipmentdb?currentSchema=inventory" --spring.datasource.username=postgresuser --spring.datasource.password=postgrespw --spring.jpa.hibernate.ddl-auto=create --spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

```
curl -X POST -H "Content-Type: application/json" -d @./resources/data/create-order-request.json http://localhost:21907/orders
```

```
curl -X PUT -H "Content-Type: application/json" -d @./resources/data/cancel-order-line-request.json http://localhost:21907/orders/1/lines/2
```
