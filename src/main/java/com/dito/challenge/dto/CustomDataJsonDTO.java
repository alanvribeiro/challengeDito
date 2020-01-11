package com.dito.challenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomDataJsonDTO {
	
	private String key;
	
	private Object value;
	
}
