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

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

/**
 * An entity mapping that represents a line item on a {@link PurchaseOrder} entity.
 *
 * @author Christian Tzolov
 */
@Entity
public class OrderLine {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_ids")
	@SequenceGenerator(name = "order_line_ids", sequenceName = "seq_order_line", allocationSize = 50)
	private Long id;

	private String item;

	private int quantity;

	private BigDecimal totalPrice;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private PurchaseOrder purchaseOrder;

	@Enumerated(EnumType.STRING)
	private OrderLineStatus status;

	public OrderLine() {
	}

	public OrderLine(String item, int quantity, BigDecimal totalPrice) {
		this.item = item;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.status = OrderLineStatus.ENTERED;
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

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public OrderLineStatus getStatus() {
		return status;
	}

	public void setStatus(OrderLineStatus status) {
		this.status = status;
	}
}