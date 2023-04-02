package com.lcwd.electronic.store.dtos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

	@NotBlank(message = "Cart Id is required !!")
	private String cartId;
	@NotBlank(message = "user Id is required !!")
	private String userId;
	
	
	private String OrderStatus="PENDING";
	private String paymentStatus="NOTPAID";	
	
	@NotBlank(message = "Address  is required !!")
	private String billingAddress;
	@NotBlank(message = "phone number is required !!")
	private String billingPhone;
	@NotBlank(message = "Billing Name is required !!")
	private String billingName;	
		


	
}
