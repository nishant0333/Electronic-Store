package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	
	@Value("${product.image.path}")
	private String imagePath;

	//create
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
		
		ProductDto productDto2 = productService.create(productDto);
		
		return new ResponseEntity<>(productDto2,HttpStatus.CREATED);	
	}
	//update
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto , @PathVariable String productId){
		ProductDto updatedproductDto = productService.update(productDto, productId);
		
		return new ResponseEntity<ProductDto>(updatedproductDto,HttpStatus.OK);
	}
	//delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){
		
		productService.delete(productId);
		
		ApiResponseMessage message = ApiResponseMessage.builder().message("product deleted successfully !!").success(true).status(HttpStatus.OK).build();
			
		return new ResponseEntity<ApiResponseMessage>(message,HttpStatus.OK);
		
	}
	//get single
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){
		
		ProductDto productDto = productService.get(productId);
		
		return new ResponseEntity<ProductDto>(productDto,HttpStatus.OK);
	}
	//get All
	@GetMapping
	public ResponseEntity<pageableResponse<ProductDto>> getAll(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		
		pageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<pageableResponse<ProductDto>>(pageableResponse,HttpStatus.OK);
	}
	//get All Live
	@GetMapping("/live")
	public ResponseEntity<pageableResponse<ProductDto>> getAllLive(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		
		pageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<pageableResponse<ProductDto>>(pageableResponse,HttpStatus.OK);
	}
	
	//search by title
	@GetMapping("/search/{query}")
	public ResponseEntity<pageableResponse<ProductDto>> searchProduct(
			@PathVariable String query ,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		
		pageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
		
		return new ResponseEntity<pageableResponse<ProductDto>>(pageableResponse,HttpStatus.OK);
	}
	
	//upload Image
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(
			@PathVariable String productId ,
			@RequestParam("productImage") MultipartFile image
			)
	{
		String fileName = fileService.uploadFile(image, imagePath);
		
		ProductDto productDto = productService.get(productId);
		
		productDto.setProductImageName(fileName);
		
		ProductDto updatedProduct = productService.update(productDto, productId);
		
		ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("product image is successfull uploaded !!").status(HttpStatus.CREATED).success(true).build();
		
	   return new ResponseEntity<ImageResponse>(response,HttpStatus.CREATED);	
	}
	
	
	//serve Image
	
	@GetMapping("/image/{productId}")
	public void serveUserImage(@PathVariable String productId , HttpServletResponse response) throws IOException {
		
		ProductDto productDto = productService.get(productId);
		
		
		
		InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
}
