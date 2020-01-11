package com.dito.challenge.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventJsonDTO {
	
	private String event;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Date timestamp;
	
	private Double revenue;
	
	private List<CustomDataJsonDTO> custom_data;
	
}
