package com.lcwd.electronic.store.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	//create order
	@PostMapping()
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request ){
		
		OrderDto createOrder = orderService.createOrder(request);
		
		return new ResponseEntity<OrderDto>(createOrder,HttpStatus.CREATED);
		}
	
	//remove order
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
		
		orderService.removeOrder(orderId);
		
		ApiResponseMessage responseMessage = ApiResponseMessage.builder()
		.message("order removed successfully !!")
		.success(true)
		.status(HttpStatus.OK)
		.build();
		return new ResponseEntity<ApiResponseMessage>(responseMessage,HttpStatus.OK);
	}
	
	//get orders of the user
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
	
		List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
		
		return new ResponseEntity<List<OrderDto>>(ordersOfUser,HttpStatus.OK);	
	}
	
	//get All order
	
	@GetMapping
	public ResponseEntity<pageableResponse<OrderDto>> getOrders(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir	
			){
	
		pageableResponse<OrderDto> response = orderService.getOrder(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<pageableResponse<OrderDto>>(response,HttpStatus.OK); 	
	}
	
}
