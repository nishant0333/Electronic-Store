package com.lcwd.electronic.store.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		
		String id = UUID.randomUUID().toString();
		
		categoryDto.setCategoryId(id);
		
		Category category = mapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepository.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@SuppressWarnings("deprecation")
	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
		
		//get Category of given id
		
		 Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id !!"));
		 
		 //update category details
		 category.setTitle(categoryDto.getTitle());
		 category.setDescription(categoryDto.getDescription());
		 category.setCoverImage(categoryDto.getCoverImage());
		 
		 Category updatedCategory = categoryRepository.save(category);
		 
		
		return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id !!"));
		
		categoryRepository.delete(category);
		
	}

	

	@Override
	public CategoryDto get(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id !!"));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public pageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findAll(pageable);
		
		pageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
		
		return pageableResponse;
	}

}
