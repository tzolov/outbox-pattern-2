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

import com.example.cdc.orderservice.model.OrderLineStatus;

/**
 * A value object that represents updating a {@link OrderLine} status.
 */
public class UpdateOrderLineRequest {

	private OrderLineStatus newStatus;

	public OrderLineStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(OrderLineStatus newStatus) {
		this.newStatus = newStatus;
	}
}