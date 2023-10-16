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

package com.example.cdc.orderservice;

import java.time.Instant;
import java.util.Optional;

import com.example.cdc.orderservice.model.EntityNotFoundException;
import com.example.cdc.orderservice.model.OrderLineStatus;
import com.example.cdc.orderservice.model.PurchaseOrder;
import com.example.cdc.orderservice.model.PurchaseOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
	
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.integration.jpa.outbox.OutboxEvent;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christian Tzolov
 */
@Service
public class OrderService implements ApplicationEventPublisherAware {

	private final PurchaseOrderRepository orderRepository;

	private ApplicationEventPublisher eventPublisher;

	private final ObjectMapper mapper;

	@Autowired
	public OrderService(PurchaseOrderRepository orderRepository, ObjectMapper mapper) {
		this.orderRepository = orderRepository;
		this.mapper = mapper;
	}

	@Transactional
	public PurchaseOrder addOrder(PurchaseOrder order) {
		PurchaseOrder order2 = orderRepository.save(order);

		OutboxEvent.builder("" + order2.getCustomerId(), order2)
				.withEventType("OrderCreated")
				.withAggregateType("Order")
				.withTimestamp(Instant.now()).build();

		// Fire events for newly created PurchaseOrder

		// 1. OrderCreated outbox event
		this.eventPublisher.publishEvent(OutboxEvent.builder("" + order2.getCustomerId(), order2)
				.withEventType("OrderCreated")
				.withAggregateType("Customer")
				.withTimestamp(Instant.now()).build());

		// 2. InvoiceCreated outbox event
		this.eventPublisher.publishEvent(OutboxEvent.builder("" + order2.getCustomerId(), order2)
				.withEventType("InvoiceCreated")
				.withAggregateType("Order")
				.withTimestamp(Instant.now()).build());

		return order2;
	}

	@Transactional
	public PurchaseOrder updateOrderLine(long orderId, long orderLineId, OrderLineStatus newStatus) {
		Optional<PurchaseOrder> order = orderRepository.findById(orderId);
		if (!order.isPresent()) {
			throw new EntityNotFoundException("Order with id " + orderId + " could not be found");
		}

		OrderLineStatus oldStatus = order.get().updateOrderLine(orderLineId, newStatus);


		// Fire events for newly created OrderLineStatus
		this.eventPublisher.publishEvent(OutboxEvent.builder(
				"" + orderId,
				this.mapper.createObjectNode()
						.put("orderId", orderId)
						.put("orderLineId", orderLineId)
						.put("oldStatus", oldStatus.name())
						.put("newStatus", newStatus.name()))
				.withEventType("OrderLineUpdated")
				.withAggregateType("Order")
				.withTimestamp(Instant.now()).build());

		return order.get();
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}
}