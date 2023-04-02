package com.lcwd.electronic.store.dtos;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

	private String imageName;
	
	private String message;
	
	private boolean success;
	
	private HttpStatus status;
}