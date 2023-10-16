# Order Service - Outbox pattern demo


sStart a pre-configured postgres server from the debezium/example-postgres:1.0 Docker image:

```
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres debezium/example-postgres:2.3.3.Final
```

You can connect to this server like this:

```
psql -U postgres -h localhost -p 5432
```

Test

```
curl -X POST -H "Content-Type: application/json" -d @./src/test/resources/data/create-order-request.json http://localhost:8080/orders
```

```
curl -X PUT -H "Content-Type: application/json" -d @./src/test/resources/data/cancel-order-line-request.json http://localhost:8080/orders/1/lines/2
```