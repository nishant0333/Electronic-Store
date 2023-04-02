package com.lcwd.electronic.store.dtos;



import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	
	
	private String categoryId;
	
	@NotBlank(message = "title is required !!")
	@Size(min = 4,message = "title must be of minimum 4 characters")
	private String title;
	@NotBlank(message = "Description required !!")
	private String description;
	@NotBlank(message = "cover image required")
	private String coverImage;
	
}
