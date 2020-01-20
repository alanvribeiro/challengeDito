package com.dito.challenge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dito.challenge.dto.EventsDTO;
import com.dito.challenge.dto.EventsJsonDTO;
import com.dito.challenge.dto.TimelineJsonDTO;
import com.dito.challenge.model.Events;
import com.dito.challenge.service.EventsService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	
	private static final String ENDPOINT_API_EVENTS_JSON = "https://storage.googleapis.com/dito-questions/events.json";
	
	@Autowired
	EventsService eventsService;
	
	@PostMapping("/collector")
	public ResponseEntity<Events> collector(@RequestBody EventsDTO eventsDTO) {
		
		Events events = converter(eventsDTO);
		return ResponseEntity.ok().body(eventsService.create(events));
		
	}
	
	private Events converter(EventsDTO eventsDTO) {
		return Events.builder()
				.event(eventsDTO.getEvent())
				.timestamp(eventsDTO.getTimestamp()).build();
	}
	
	@GetMapping("/autocomplete/{keyword}")
	public ResponseEntity<List<String>> autocomplete(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok().body(eventsService.search(keyword));
	}
	
	@GetMapping("/dataMnipulation")
	public ResponseEntity<List<TimelineJsonDTO>> dataMnipulation() throws Exception {
		
		String json = converterUrlFromJson(ENDPOINT_API_EVENTS_JSON);
        EventsJsonDTO eventsJson = new Gson().fromJson(json, EventsJsonDTO.class);
        
        return ResponseEntity.ok().body(eventsService.dataMnipulation(eventsJson));
		
	}
	
	private String converterUrlFromJson(String urlJson) throws IOException {
		
		StringBuilder sbJson = new StringBuilder();
		URL url = new URL(urlJson);
		
	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
	    
	    String strBufferedReader;
	    while((strBufferedReader = bufferedReader.readLine()) != null){
	    	sbJson.append(strBufferedReader); 
	    }
	    
	    bufferedReader.close();

	    return sbJson.toString();
	    
	}
	
}
