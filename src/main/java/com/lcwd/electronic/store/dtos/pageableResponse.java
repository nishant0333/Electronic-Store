package com.lcwd.electronic.store.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class pageableResponse<T> {

	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long totelElements;
	private int totalpages;
	private boolean lastpage;
	
	
	
}
