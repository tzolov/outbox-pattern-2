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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.cdc.orderservice.model.OrderLine;
import com.example.cdc.orderservice.model.PurchaseOrder;

/**
 * A value object that represents a request to create a {@link PurchaseOrder}.
 *
 * @author Christian Tzolov

 */
public class CreateOrderRequest {

	private long customerId;
	private LocalDateTime orderDate;
	private List<OrderLineDto> lineItems;

	public CreateOrderRequest() {
		this.lineItems = new ArrayList<>();
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderLineDto> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<OrderLineDto> lineItems) {
		this.lineItems = lineItems;
	}

	public PurchaseOrder toOrder() {
		List<OrderLine> lines = lineItems.stream()
				.map(l -> new OrderLine(l.getItem(), l.getQuantity(), l.getTotalPrice()))
				.collect(Collectors.toList());

		return new PurchaseOrder(customerId, orderDate, lines);
	}
}
