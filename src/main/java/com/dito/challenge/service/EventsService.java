package com.dito.challenge.service;

import java.util.List;

import com.dito.challenge.model.Events;

public interface EventsService {
	
	public Events create(Events events);
	
	public List<String> search(String keyword);
	
}
