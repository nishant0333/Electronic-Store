package com.lcwd.electronic.store.dtos;

import java.sql.Date;

import com.lcwd.electronic.store.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {

	

	private String productId;
	
	private String title;
	
	
	private String description;
	
	private int price;
	
	private int descountedPrice;
	
	private int quantity;
	
	private Date addedDate;
	
	private boolean live;
	
	private boolean stock;
	
	private String productImageName;
	
	private CategoryDto category;
	
	
}
