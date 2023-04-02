package com.lcwd.electronic.store.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.ProductService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;

	// create

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

		CategoryDto categoryDto1 = categoryService.create(categoryDto);

		return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
	}

	// update
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable String categoryId) {

		CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);

		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

	// delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {

		categoryService.delete(categoryId);

		ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully")
				.status(HttpStatus.OK).success(true).build();

		return new ResponseEntity<ApiResponseMessage>(responseMessage, HttpStatus.OK);
	}

	// getAll
	@GetMapping
	public ResponseEntity<pageableResponse<CategoryDto>> getAll(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			) {

		pageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<pageableResponse<CategoryDto>>(pageableResponse, HttpStatus.OK);

	}
	// get Single

	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
		CategoryDto categoryDto = categoryService.get(categoryId);

		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	//create product with category
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(
			@PathVariable("categoryId") String categoryId ,
			@RequestBody ProductDto dto
			){
		
		ProductDto productWithCategory = productService.createWithCategory(dto, categoryId);
		
	return new ResponseEntity<ProductDto>(productWithCategory,HttpStatus.CREATED);	
	}
	
	//update category of product
	
	@PutMapping("/{categoryId}/products/{productsId}")
	public ResponseEntity<ProductDto> updateCategoryOfProduct(
			@PathVariable("categoryId") String categoryId ,
			@PathVariable("productsId") String productsId
			){
		
		ProductDto productDto = productService.updateCategory(productsId, categoryId);
		
		return new ResponseEntity<ProductDto>(productDto,HttpStatus.OK);
	}
	
	//get All product of a category
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<pageableResponse<ProductDto>> getProductOfCategory(
			@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			
			){
		
		pageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<pageableResponse<ProductDto>>(response, HttpStatus.OK);
	}

}
