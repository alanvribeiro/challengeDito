package com.dito.challenge.service;

import java.util.List;

import com.dito.challenge.dto.EventsJsonDTO;
import com.dito.challenge.dto.TimelineJsonDTO;
import com.dito.challenge.model.Events;

public interface EventsService {
	
	public Events create(Events events);
	
	public List<String> search(String keyword);
	
	public List<TimelineJsonDTO> dataMnipulation(EventsJsonDTO eventsJson) throws Exception;
	
}
