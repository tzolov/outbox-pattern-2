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

package com.example.cdc.orderservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import jakarta.persistence.Entity;

/**
 * An entity mapping that represents a purchase order.
 *
 * @author Christian Tzolov
 */
@Entity
public class PurchaseOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_ids")
	@SequenceGenerator(name = "purchase_order_ids", sequenceName = "seq_purchase_order", allocationSize = 50)
	private Long id;

	private long customerId;

	private LocalDateTime orderDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "purchaseOrder")
	private List<OrderLine> lineItems;

	PurchaseOrder() {
	}

	public PurchaseOrder(long customerId, LocalDateTime orderDate, List<OrderLine> lineItems) {
		this.customerId = customerId;
		this.orderDate = orderDate;
		this.lineItems = new ArrayList<>(lineItems);
		lineItems.forEach(line -> line.setPurchaseOrder(this));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<OrderLine> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<OrderLine> lineItems) {
		this.lineItems = lineItems;
	}

	public OrderLineStatus updateOrderLine(long orderLineId, OrderLineStatus newStatus) {
		for (OrderLine orderLine : lineItems) {
			if (orderLine.getId() == orderLineId) {
				OrderLineStatus oldStatus = orderLine.getStatus();
				orderLine.setStatus(newStatus);
				return oldStatus;
			}
		}

		throw new EntityNotFoundException("Order does not contain line with id " + orderLineId);
	}

	public BigDecimal getTotalValue() {
		return lineItems.stream().map(OrderLine::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}