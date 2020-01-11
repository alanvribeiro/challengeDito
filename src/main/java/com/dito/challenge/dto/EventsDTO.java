package com.dito.challenge.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventsDTO {
	
    private String event;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
	private Date timestamp;
    
}
