package com.lcwd.electronic.store.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	private String imageName;
	
	
	private Logger logger=LoggerFactory.getLogger(UserController.class);

	// create

	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

		UserDto userDto1 = userService.createUser(userDto);

		return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDtom, @PathVariable String userId) {

		UserDto updatedUserDto = userService.updateUser(userDtom, userId);

		return new ResponseEntity<UserDto>(updatedUserDto, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);

		ApiResponseMessage message = ApiResponseMessage.builder().message("User is Deleted Successfully !")
				.success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}
	// get all

	@GetMapping
	public ResponseEntity<pageableResponse<UserDto>> getAllUser(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	// get single
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		return new ResponseEntity<UserDto>(userService.getUserById(userId), HttpStatus.OK);
	}

	// get by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {

		return new ResponseEntity<UserDto>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	// search user
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords) {
		return new ResponseEntity<List<UserDto>>(userService.searchUser(keywords), HttpStatus.OK);
	}

	// upload user image

	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,
			@PathVariable String userId)  {
		
		
		UserDto user1 = userService.getUserById(userId);
		
		//boolean blank = user1.getImageName().isBlank();
		
		
			
			if (!user1.getImageName().isBlank()) {
				
				String fullpath = imageUploadPath + user1.getImageName();
				
				try {

					Path path = Paths.get(fullpath);
					Files.delete(path);
					
				} catch (NoSuchFileException e) {
					logger.info("User image not found in foulder ");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				imageName = fileService.uploadFile(image, imageUploadPath);
				
			} else {
			 imageName = fileService.uploadFile(image, imageUploadPath);
			}
			
		
		
		

		

		//UserDto user = userService.getUserById(userId);
		user1.setImageName(imageName);

		UserDto userDto = userService.updateUser(user1, userId);

		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("image uploade successful")
				.success(true).status(HttpStatus.CREATED).build();

		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}

	// serve user image
	
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId , HttpServletResponse response) throws IOException {
		
		UserDto user = userService.getUserById(userId);
		
		logger.info("user image name : {} ", user.getImageName());
		
		InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
