/*
 * Copyright 2023-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cdc.shipmentservice;

import java.time.Instant;
import java.time.LocalDateTime;

import com.example.cdc.shipmentservice.model.Shipment;
import com.example.cdc.shipmentservice.model.ShipmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christian Tzolov
 */
@Service
public class ShipmentService {

	private static final Logger logger = Logger.getLogger(ShipmentService.class.getName());

	// private final EventDeduplication deduplicationService;

	private final ShipmentRepository shipmentRepository;

	@Autowired
	public ShipmentService(ShipmentRepository shipmentRepository) {
		// this.deduplicationService = deduplicationService;
		this.shipmentRepository = shipmentRepository;
	}

	@Transactional
	public void onOrderEvent(UUID eventId, String eventType, String key, JsonNode payload, Instant ts) {

		// if (this.deduplicationService.alreadyProcessed(eventId)) {
		// 	logger.warn("Event with UUID {" + eventId + "} was already retrieved, ignoring it");
		// 	return;
		// }

		if (eventType.equals("OrderCreated")) {
			this.orderCreated(payload);
		}
		else if (eventType.equals("OrderLineUpdated")) {
			this.orderLineUpdated(payload);
		}
		else {
			// logger.warn("Unknown event type: " + eventType);
		}

		// this.deduplicationService.processed(eventId);
	}

	private void orderCreated(JsonNode payload) {
		logger.info("Order created: " + payload.toPrettyString());

		final long orderId = payload.get("id").asLong();
		final long customerId = payload.get("customerId").asLong();
		final LocalDateTime orderDate = LocalDateTime.parse(payload.get("orderDate").asText());

		this.shipmentRepository.save(new Shipment(customerId, orderId, orderDate));

	}

	private void orderLineUpdated(JsonNode payload) {
		logger.info("Order Line updated: " + payload.toPrettyString());
	}
}
