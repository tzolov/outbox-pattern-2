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

package com.example.cdc.orderservice.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.cdc.orderservice.model.PurchaseOrder;

/**
 * A value object that represents the response of an operation on a {@link PurchaseOrder}.
 */
public class OrderOperationResponse {

	private final long id;
	private final long customerId;
	private final LocalDateTime orderDate;
	private final List<OrderLineDto> lineItems;

	public OrderOperationResponse(long id, long customerId, LocalDateTime orderDate, List<OrderLineDto> lineItems) {
		this.id = id;
		this.customerId = customerId;
		this.orderDate = orderDate;
		this.lineItems = lineItems;
	}

	public static OrderOperationResponse from(PurchaseOrder order) {
		List<OrderLineDto> lines = order.getLineItems()
				.stream()
				.map(l -> new OrderLineDto(l.getId(), l.getItem(), l.getQuantity(), l.getTotalPrice(), l.getStatus()))
				.collect(Collectors.toList());

		return new OrderOperationResponse(order.getId(), order.getCustomerId(), order.getOrderDate(), lines);
	}

	public long getId() {
		return id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public List<OrderLineDto> getLineItems() {
		return lineItems;
	}
}