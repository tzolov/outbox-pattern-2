package com.example.cdc.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.jpa.outbox.OutboxApplicationListener;

@SpringBootApplication
@EnableJpaRepositories({ "com.example.cdc.orderservice.model",
		"org.springframework.integration.jpa.outbox" })
@EntityScan({ "com.example.cdc.orderservice.model", "org.springframework.integration.jpa.outbox" })

public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public OutboxApplicationListener outboxApplicationListener(EntityManager entityManager, ObjectMapper objectMapper) {
		return new OutboxApplicationListener(entityManager, objectMapper, false);
	}

	@Bean
	public ObjectMapper objectMapper() {
		var objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

}
