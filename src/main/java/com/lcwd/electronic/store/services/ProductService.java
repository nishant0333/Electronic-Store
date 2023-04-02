package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.pageableResponse;

public interface ProductService {

	
	//create
	ProductDto create(ProductDto prodectDto);
	//update 
	ProductDto update(ProductDto prodectDto, String productId);
	//delete
	void delete(String productId);
	//get single
	ProductDto get(String productId);
	//get all
	pageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
	//get all live
	pageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
	//search product
	pageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);
	
	//create product with category
	
	ProductDto createWithCategory(ProductDto productDto , String categoryId);
	
	//update Category of product
	
	ProductDto updateCategory(String productId , String categoryId);
	
	//get All product of a category
	pageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);
	
	
}
