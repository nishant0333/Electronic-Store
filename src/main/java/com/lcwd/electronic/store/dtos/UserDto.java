package com.lcwd.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

	private String userId;

	@Size(min = 3,max = 25,message = "Invalid Name !!")
	private String name;

	//@Email(message = "Invalid user Email !!")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid user Email !!")
	@NotBlank(message = "Email is Required !!")
	private String email;

	@NotBlank(message = "password is required !!")
	private String password;
	
	@Size(min = 4,max = 6,message = "invalid Gender !!")
	private String gender;

	@NotBlank(message = "Write Something about yourself !!")
	private String about;

	@ImageNameValid
	private String imageName;
	
	private Set<RoleDto> roles=new HashSet<>();
}
