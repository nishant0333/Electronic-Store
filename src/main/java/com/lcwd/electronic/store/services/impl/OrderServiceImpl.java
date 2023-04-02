package com.lcwd.electronic.store.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CartRepository cartRepository;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderRequest) {
		
		String userId = orderRequest.getUserId();
		String cartId = orderRequest.getCartId();
		
		//fetch user
		User user = userRepository.findById(userId)
				.orElseThrow(()->new ResourceNotFoundException("User not found with given id!!"));
		
		
		//fetch cart
		
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(()->new ResourceNotFoundException("Cart not found with given id!!"));
		
		List<CartItem> cartItems = cart.getItems();
		
		if(cartItems.size()<=0) {
			throw new BadApiRequestException("Invalid number of items in cart !!");
		}
		
		
		Order order = Order.builder()
		.billingName(orderRequest.getBillingName())
		.billingPhone(orderRequest.getBillingPhone())
		.billingAddress(orderRequest.getBillingAddress())
		.orderedDate(new Date(System.currentTimeMillis()))
		.deliveredDate(null)
		.paymentStatus(orderRequest.getPaymentStatus())
		.OrderStatus(orderRequest.getOrderStatus())
		.orderId(UUID.randomUUID().toString())
		.user(user)
		.build();
		
		//orderItems,Amount
		
		AtomicReference<Integer> orderAmount=new AtomicReference<>(0);
		
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
		
			//CartItem -> orderItem
			OrderItem orderItem = OrderItem.builder()
			.quentity(cartItem.getQuantity())
			.product(cartItem.getProduct())
			.totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDescountedPrice())
			.order(order)
			.build();
			
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
		
			return orderItem;
		
		}).collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
		//
		cart.getItems().clear();
		
		cartRepository.save(cart);
		Order savedOrder = orderRepository.save(order);
		
		
		return modelMapper.map(savedOrder, OrderDto.class);
	}
	
	
	

	@Override
	public void removeOrder(String orderId) {
		
		Order order = orderRepository.findById(orderId)
				.orElseThrow(()-> new ResourceNotFoundException("order not found with given id!!"));
		
		
		orderRepository.delete(order);
		
		
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		
		 User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
	        List<Order> orders = orderRepository.findByUser(user);
	        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
	        return orderDtos;
	}

	@Override
	public pageableResponse<OrderDto> getOrder(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort=(sortDir.equalsIgnoreCase("desc")) ?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		
		Page<Order> page = orderRepository.findAll(pageable);
		
		return Helper.getPageableResponse(page,OrderDto.class);
	}

}
