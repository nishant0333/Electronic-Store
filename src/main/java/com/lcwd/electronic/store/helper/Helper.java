package com.lcwd.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.dtos.pageableResponse;
import com.lcwd.electronic.store.entities.User;

public class Helper {
       
	
	//U->your Entity
    //V-> is Your Dto
	public static <U,V> pageableResponse<V> getPageableResponse(Page<U> page,Class <V> type){
		
		List<U> entity = page.getContent();

		List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
		
		pageableResponse<V> response=new pageableResponse<>();
		
		response.setContent(dtoList);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotelElements(page.getTotalElements());
		response.setTotalpages(page.getTotalPages());
		response.setLastpage(page.isLast());
		
		return response;
	}
	
}
