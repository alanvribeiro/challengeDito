package com.dito.challenge.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimelineJsonDTO {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Date timestamp;
	
	private Double revenue;
	
	private String transaction_id;
	
	private String store_name;
	
	private List<ProductsJsonDTO> products;
	
}
