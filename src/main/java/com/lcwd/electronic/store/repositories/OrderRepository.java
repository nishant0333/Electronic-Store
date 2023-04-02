package com.lcwd.electronic.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.User;

public interface OrderRepository extends JpaRepository<Order,String> {

	List<Order> findByUser(User user);
	
}
