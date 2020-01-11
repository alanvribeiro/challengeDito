package com.dito.challenge.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dito.challenge.error.BusinessRuleException;
import com.dito.challenge.model.Events;
import com.dito.challenge.repository.EventsRepository;
import com.dito.challenge.service.EventsService;

@Service
public class EventsServiceImpl implements EventsService {
	
	@Autowired
	private EventsRepository eventsRepository;
	
	@Override
	@Transactional
	public Events create(Events events) {
		
		validateEvents(events);
		return eventsRepository.save(events);
		
	}
	
	@Override
	public List<String> search(String keyword) {
		return eventsRepository.search(keyword);
	}
	
	private void validateEvents(Events events) {
		
		if(events.getEvent() == null || events.getEvent().equals("")) {
			throw new BusinessRuleException("Event name is required.");
		}
		
		if(events.getTimestamp() == null) {
			throw new BusinessRuleException("Event collection date is required.");
		}
		
	}
	
}
