package com.lcwd.electronic.store.services.impl;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService {
	
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
	
		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		if(quantity<=0) {
			throw new BadApiRequestException("requested quantity not valid");
		}
		
		//fetch the product
		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new ResourceNotFoundException("product not found with given id !!"));
		
		//fetch the user form Db
		User user = userRepository.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("user is not found with given id"));
		
		Cart cart = null;
		
		try {
			cart = cartRepository.findByUser(user).get();
		} catch (NoSuchElementException e) {
			cart= new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			
			long now = System.currentTimeMillis();
			cart.setCreatedAt(new Date(now));
		}
		
		//perform cart operations
		//if cart items already present then update
		
		
		//inside the map method of stream we can not change update value for 
		//that we use AtomicRefrence
		AtomicReference<Boolean> updated=new AtomicReference<>(false);
		
		List<CartItem> items = cart.getItems();
		
		items = items.stream().map(item -> {
			if(item.getProduct().getProductId().equals(productId)) {
				//item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDescountedPrice());
				updated.set(true);
			}
			
			
			return item;
		}).collect(Collectors.toList());
		
		//cart.setItems(updatedItems);
		
		//create items
		if(!updated.get()) {
			CartItem cartItem = CartItem.builder()
					.quantity(quantity)
					.totalPrice(quantity*product.getDescountedPrice())
					.cart(cart)
					.product(product)
					.build();
					cart.getItems().add(cartItem);
		}
		
		
		
		cart.setUser(user);
		Cart updatedCart = cartRepository.save(cart);
		
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		//conditions
		
		
		CartItem cartItem2 = cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFoundException("cartItem not found with given id!!"));
		cartItemRepository.delete(cartItem2);
		
	}

	@Override
	public void clearCart(String userId) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user is not found with given id"));
	    Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("cart of given user not found"));
	    cart.getItems().clear();
	    cartRepository.save(cart);
	
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user is not found with given id"));
	    Cart cart = cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("cart of given user not found"));
		return mapper.map(cart, CartDto.class);
	}

}
