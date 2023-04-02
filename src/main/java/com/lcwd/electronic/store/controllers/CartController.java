package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartService cartService;
	//add items to cart
	
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,@RequestBody AddItemToCartRequest request){
		
		CartDto cartDto = cartService.addItemToCart(userId, request);
		return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{UserId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(
			@PathVariable String UserId,
			@PathVariable int itemId
			){
		
		cartService.removeItemFromCart(UserId, itemId);
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message("item is removed !!")
				.success(true)
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.OK);
	}
	
	
	
	@DeleteMapping("/{UserId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String UserId){
		
		cartService.clearCart(UserId);
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message("cart clear !!")
				.success(true)
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCart(@PathVariable String userId){
		
		CartDto cartDto = cartService.getCartByUser(userId);
		return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
		
	}
	
}
