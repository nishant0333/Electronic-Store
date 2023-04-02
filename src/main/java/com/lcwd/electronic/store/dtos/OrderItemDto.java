package com.lcwd.electronic.store.dtos;


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
public class OrderItemDto {

	
	private int orderItemId;
	private int quentity;	
	private int totalPrice;	
	private ProductDto product;
	
	
}
