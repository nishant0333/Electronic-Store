package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.query.criteria.internal.expression.function.CurrentDateFunction;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper mapper;

	@Value("${product.image.path}")
	private String imagePath;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ProductDto create(ProductDto prodectDto) {

		Product product = mapper.map(prodectDto, Product.class);

		// product Id
		String id = UUID.randomUUID().toString();
		product.setProductId(id);

		// added Date
		long now = System.currentTimeMillis();
		product.setAddedDate(new Date(now));

		Product saveProduct = productRepository.save(product);

		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto prodectDto, String productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with given Id !!"));
		product.setTitle(prodectDto.getTitle());
		product.setDescription(prodectDto.getDescription());
		product.setPrice(prodectDto.getPrice());
		product.setDescountedPrice(prodectDto.getDescountedPrice());
		product.setQuantity(prodectDto.getQuantity());
		product.setLive(prodectDto.isLive());
		product.setStock(prodectDto.isStock());
		product.setProductImageName(prodectDto.getProductImageName());

		Product savedProduct = productRepository.save(product);

		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public void delete(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with given Id !!"));

		// delete image

		String fullpath = imagePath + product.getProductImageName();

		try {

			Path path = Paths.get(fullpath);
			Files.delete(path);

		} catch (NoSuchFileException e) {
			logger.info("User image not found in foulder ");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// delete product
		productRepository.delete(product);

	}

	@Override
	public ProductDto get(String productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with given Id !!"));

		return mapper.map(product, ProductDto.class);
	}

	@Override
	public pageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepository.findAll(pageable);

		pageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

		return pageableResponse;
	}

	@Override
	public pageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepository.findByLiveTrue(pageable);

		pageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

		return pageableResponse;
	}

	@Override
	public pageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);

		pageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);

		return pageableResponse;
	}

	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

		// fetch the category from db

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id!!"));

		Product product = mapper.map(productDto, Product.class);

		// product Id
		String id = UUID.randomUUID().toString();
		product.setProductId(id);

		// added Date
		long now = System.currentTimeMillis();
		product.setAddedDate(new Date(now));
		
		//Add category
		product.setCategory(category);

		Product saveProduct = productRepository.save(product);

		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateCategory(String productId, String categoryId) {

		//product fetch
		
		Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product is not found with given id !!"));
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));
		
		product.setCategory(category);
		
		Product savedProduct = productRepository.save(product);
		
		
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public pageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		
		
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));
		
		Page<Product> page = productRepository.findByCategory(category, pageable);
		
		
		return Helper.getPageableResponse(page, ProductDto.class);
	}

}
