package com.lcwd.electronic.store.services;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;


@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	User user;
	Role role;
	
	String roleId;
	
	@BeforeEach
	public void init() {
		
		role=Role.builder()
				.roleId("abc")
				.roleName("NORMAL").build();
		
		
		user = User.builder()
		.name("Durgesh")
		.email("durgesh@gmail.com")
		.about("this is testing create method")
		.password("abcd")
		.imageName("abc.png")
		.roles(Set.of(role))
		.build();
		
		
		roleId="abc";
		
	}
	
	//create User
	
	@Test
	public void createUserTest() {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
	
	UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
	
	System.out.println(user1.getName());
	
	Assertions.assertNotNull(user1);
	Assertions.assertEquals("Durgesh", user1.getName());
	
	
	}
	
	public void updateUser() {
		
		
	}
	

}
