package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.pageableResponse;

public interface OrderService {

	//create order
	OrderDto createOrder(CreateOrderRequest orderRequest);
	
	
	//remove order
	void removeOrder(String orderId);
	
	
	//get orders of user
	List<OrderDto> getOrdersOfUser(String userId);
	
	
	//get orders
	pageableResponse<OrderDto> getOrder(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
	//other service related to order
	
	
	
}
