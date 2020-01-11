package com.dito.challenge.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventsJsonDTO {

	private List<EventJsonDTO> events;
	
}
