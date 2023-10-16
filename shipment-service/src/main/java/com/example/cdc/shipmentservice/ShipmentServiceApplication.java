package com.example.cdc.shipmentservice;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.debezium.outbox.OutboxMessageRelay;

@SpringBootApplication
public class ShipmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentServiceApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		var objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	@Bean
	public OutboxMessageRelay outboxMessageRelay(ShipmentService shipmentService,
			DebeziumEngine.Builder<ChangeEvent<byte[], byte[]>> engineBuilder,
			ObjectMapper objectMapper) {

		return new OutboxMessageRelay(engineBuilder, message -> {
			// System.out.println(new String((byte[]) message.getPayload()));

			try {
				Map<String, String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);

				UUID eventId = UUID.fromString(map.get("id"));
				String aggregateId = map.get("aggregate_id");
				String type = map.get("type");
				Instant timestamp = Instant.now();
				// .ofEpochMilli(message.getHeaders().get("kafka_receivedTimestamp", Long.class));
				// logger.info("Map:" + map);
				JsonNode jsonNode = deserialize(objectMapper, map.get("aggregate"));

				shipmentService.onOrderEvent(eventId, type, aggregateId, jsonNode, timestamp);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static JsonNode deserialize(ObjectMapper objectMapper, String event) {
		try {
			JsonNode eventPayload = objectMapper.readTree(event);
			return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't deserialize event", e);
		}

	}

	// @Bean
	// public OutboxMessageRelay outboxMessageRelay(ShipmentService shipmentService, ObjectMapper objectMapper) {
	// 	return new OutboxMessageRelay(OUTBOX_TABLE_NAME, connectorConfig(5432),
	// 			message -> {
	// 				// System.out.println(new String((byte[]) message.getPayload()));

	// 				try {
	// 					Map<String, String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);

	// 					UUID eventId = UUID.fromString(map.get("id"));
	// 					String aggregateId = map.get("aggregate_id");
	// 					String type = map.get("type");
	// 					Instant timestamp = Instant.now();
	// 					// .ofEpochMilli(message.getHeaders().get("kafka_receivedTimestamp", Long.class));
	// 					// logger.info("Map:" + map);
	// 					JsonNode jsonNode = deserialize(objectMapper, map.get("aggregate"));

	// 					shipmentService.onOrderEvent(eventId, type, aggregateId, jsonNode, timestamp);
	// 				}
	// 				catch (IOException e) {
	// 					e.printStackTrace();
	// 				}
	// 			});
	// }

	// /**
	//  * Fully qualified Outbox table name.
	//  */
	// private static final String OUTBOX_TABLE_NAME = "public.outbox";

	// private static Properties connectorConfig(int port) {
	// 	Properties config = new Properties();

	// 	config.put("schema.history.internal", "io.debezium.relational.history.MemorySchemaHistory");
	// 	config.put("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore");

	// 	UUID uuid = UUID.randomUUID();
	// 	config.put("name", "my-connector-" + uuid);

	// 	// Topic prefix for the database server or cluster.
	// 	config.put("topic.prefix", "my-topic-" + uuid);
	// 	// Unique ID of the connector.
	// 	config.put("database.server.id", "" + (uuid.getMostSignificantBits() & Long.MAX_VALUE));

	// 	config.put("key.converter.schemas.enable", "false");
	// 	config.put("value.converter.schemas.enable", "false");

	// 	config.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
	// 	config.put("database.user", "postgres");
	// 	config.put("database.password", "postgres");
	// 	config.put("database.dbname", "postgres");
	// 	config.put("database.hostname", "localhost");
	// 	config.put("database.port", String.valueOf(port));

	// 	return config;
	// }

}
