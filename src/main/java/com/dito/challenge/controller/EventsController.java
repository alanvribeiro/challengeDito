package com.dito.challenge.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dito.challenge.dto.CustomDataJsonDTO;
import com.dito.challenge.dto.EventJsonDTO;
import com.dito.challenge.dto.EventsDTO;
import com.dito.challenge.dto.EventsJsonDTO;
import com.dito.challenge.dto.ProductsJsonDTO;
import com.dito.challenge.dto.TimelineJsonDTO;
import com.dito.challenge.model.Events;
import com.dito.challenge.service.EventsService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	
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
	public ResponseEntity< Map<String, List<TimelineJsonDTO>>> dataMnipulation() throws Exception {
		
		List<TimelineJsonDTO> timelines = new ArrayList<>();
		
		String json = converterUrlFromJson("https://storage.googleapis.com/dito-questions/events.json");
        EventsJsonDTO eventsJson = new Gson().fromJson(json, EventsJsonDTO.class);
		
        for(EventJsonDTO eventJson : eventsJson.getEvents()) {
        	
        	TimelineJsonDTO timeline = new TimelineJsonDTO();
        	timeline.setTimestamp(eventJson.getTimestamp());
        	timeline.setRevenue(eventJson.getRevenue());
        	timeline = converterCustomData(timeline, eventJson);
        	
        	timelines.add(timeline);
        	
        }
        
        Map<String, List<TimelineJsonDTO>> groupingBy = timelines.stream().collect(
                Collectors.groupingBy(TimelineJsonDTO::getTransaction_id, Collectors.toList()));
        
		return ResponseEntity.ok().body(groupingBy);
		
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
	
	private TimelineJsonDTO converterCustomData(TimelineJsonDTO timeline, EventJsonDTO eventJson) {
		
		ProductsJsonDTO products = new ProductsJsonDTO();
		List<ProductsJsonDTO> listProducts = new ArrayList<>();
		
		for(CustomDataJsonDTO custom_data : eventJson.getCustom_data()) {
			
			if(custom_data.getKey().equals("transaction_id")) {
    			timeline.setTransaction_id((String) custom_data.getValue());
    		}
			else if(eventJson.getEvent().equals("comprou") && custom_data.getKey().equals("store_name")) {
	    			timeline.setStore_name((String) custom_data.getValue());
    		}
			else if(eventJson.getEvent().equals("comprou-produto")) {

				if(custom_data.getKey().equals("product_name")) {
					products.setName((String) custom_data.getValue());
	    		}
				else if(custom_data.getKey().equals("product_price")) {
					products.setPrice((Double) custom_data.getValue());
	    		}
				
			}
			
		}
		
		if(products.getName() != null || products.getPrice() != null) {
			listProducts.add(products);
			timeline.setProducts(listProducts);
		}
		
    	return timeline;
	}
	
}
