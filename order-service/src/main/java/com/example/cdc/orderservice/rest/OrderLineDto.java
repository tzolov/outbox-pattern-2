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

import java.math.BigDecimal;

import com.example.cdc.orderservice.model.OrderLineStatus;

/**
 * A value object that represents an OrderLine for a PurchaseOrder.
 */
public class OrderLineDto {

	private Long id;
	private String item;
	private int quantity;
	private BigDecimal totalPrice;
	private OrderLineStatus status;

	public OrderLineDto() {
	}

	public OrderLineDto(String item, int quantity, BigDecimal totalPrice) {
		this.item = item;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.status = OrderLineStatus.ENTERED;
	}

	public OrderLineDto(long id, String item, int quantity, BigDecimal totalPrice, OrderLineStatus status) {
		this.id = id;
		this.item = item;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderLineStatus getStatus() {
		return status;
	}

	public void setStatus(OrderLineStatus status) {
		this.status = status;
	}
}
