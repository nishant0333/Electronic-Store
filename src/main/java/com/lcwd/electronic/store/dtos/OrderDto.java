package com.lcwd.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderDto {

	
	private String orderId;
	private String OrderStatus="PENDING";
	private String paymentStatus="NOTPAID";	
	private int orderAmount;
	private String billingAddress;	
	private String billingPhone;	
	private String billingName;	
	private Date orderedDate=new Date(System.currentTimeMillis());	
	private Date deliveredDate;	
	//private UserDto userDto;	
	private List<OrderItemDto> orderItems=new ArrayList<>();
}
