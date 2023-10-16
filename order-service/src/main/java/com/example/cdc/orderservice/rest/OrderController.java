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

import com.example.cdc.orderservice.OrderService;
import com.example.cdc.orderservice.model.PurchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Christian Tzolov
 */
@RestController
public class OrderController {

	private final OrderService orderService;

	public OrderController(@Autowired OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/orders")
	public OrderOperationResponse addOrder(@RequestBody CreateOrderRequest createOrderRequest) {
		PurchaseOrder order = createOrderRequest.toOrder();
		order = this.orderService.addOrder(order);
		return OrderOperationResponse.from(order);
	}

	@PutMapping("/orders/{orderId}/lines/{orderLineId}")
	public OrderOperationResponse updateOrderLine(@PathVariable long orderId, @PathVariable long orderLineId,
			@RequestBody UpdateOrderLineRequest request) {
		PurchaseOrder updated = this.orderService.updateOrderLine(orderId, orderLineId, request.getNewStatus());
		return OrderOperationResponse.from(updated);
	}
}