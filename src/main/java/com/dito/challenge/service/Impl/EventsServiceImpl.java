package com.dito.challenge.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dito.challenge.dto.CustomDataJsonDTO;
import com.dito.challenge.dto.EventJsonDTO;
import com.dito.challenge.dto.EventsJsonDTO;
import com.dito.challenge.dto.ProductsJsonDTO;
import com.dito.challenge.dto.TimelineJsonDTO;
import com.dito.challenge.error.BusinessRuleException;
import com.dito.challenge.model.Events;
import com.dito.challenge.repository.EventsRepository;
import com.dito.challenge.service.EventsService;

@Service
public class EventsServiceImpl implements EventsService {
	
	private static final String TRANSACTION_ID = "transaction_id";
	private static final String STORE_NAME = "store_name";
	private static final String PRODUCT_PRICE = "product_price";
	private static final String PRODUCT_NAME = "product_name";
	private static final String COMPROU_PRODUTO = "comprou-produto";
	private static final String COMPROU = "comprou";
	
	@Autowired
	private EventsRepository eventsRepository;
	
	@Override
	@Transactional
	public Events create(Events events) {
		validateEvents(events);
		return eventsRepository.save(events);
	}
	
	private void validateEvents(Events events) {
		
		if(events.getEvent() == null || events.getEvent().equals("")) {
			throw new BusinessRuleException("Event name is required.");
		}
		
		if(events.getTimestamp() == null) {
			throw new BusinessRuleException("Event collection date is required.");
		}
		
	}
	
	@Override
	public List<String> search(String keyword) {
		return eventsRepository.search(keyword);
	}
	
	@Override
	public List<TimelineJsonDTO> dataMnipulation(EventsJsonDTO eventsJson) throws Exception {
		
		List<TimelineJsonDTO> timelines = new ArrayList<>();
		
        for(EventJsonDTO eventJson : eventsJson.getEvents()) {
        	
        	TimelineJsonDTO timeline = new TimelineJsonDTO();
        	timeline.setTimestamp(eventJson.getTimestamp());
        	timeline.setRevenue(eventJson.getRevenue());
        	timeline = converterCustomData(timeline, eventJson);
        	
        	timelines.add(timeline);
        	
        }
        
        timelines = groupByTransaction(timelines);
        timelines = orderByTimestampDesc(timelines);
        
		return timelines;
		
	}
	
	private TimelineJsonDTO converterCustomData(TimelineJsonDTO timeline, EventJsonDTO eventJson) {
		
		ProductsJsonDTO products = new ProductsJsonDTO();
		List<ProductsJsonDTO> listProducts = new ArrayList<>();
		
		for(CustomDataJsonDTO custom_data : eventJson.getCustom_data()) {
			
			if(custom_data.getKey().equals(TRANSACTION_ID)) {
    			timeline.setTransaction_id((String) custom_data.getValue());
    		}
			else if(eventJson.getEvent().equals(COMPROU) && custom_data.getKey().equals(STORE_NAME)) {
	    			timeline.setStore_name((String) custom_data.getValue());
    		}
			else if(eventJson.getEvent().equals(COMPROU_PRODUTO)) {

				if(custom_data.getKey().equals(PRODUCT_NAME)) {
					products.setName((String) custom_data.getValue());
	    		}
				else if(custom_data.getKey().equals(PRODUCT_PRICE)) {
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
	
	private List<TimelineJsonDTO> groupByTransaction(List<TimelineJsonDTO> listTimelineJsonDTO) {
		
		Map<String, TimelineJsonDTO> mapTimelineJsonDTO = new HashMap<>();
		List<TimelineJsonDTO> groupListTimelineJsonDTO = new ArrayList<>();
		
		for (TimelineJsonDTO timelineJsonDTO : listTimelineJsonDTO) {
			
			String key = timelineJsonDTO.getTransaction_id();
			
			if(mapTimelineJsonDTO.containsKey(key)){
				
				TimelineJsonDTO timelineJsonDTOMap = mapTimelineJsonDTO.get(key);
				
				if(timelineJsonDTO.getRevenue() != null) {
					timelineJsonDTOMap.setRevenue(timelineJsonDTO.getRevenue());
				}
				
				if(timelineJsonDTO.getStore_name() != null) {
					timelineJsonDTOMap.setStore_name(timelineJsonDTO.getStore_name());
				}
				
				if(timelineJsonDTO.getProducts() != null && !timelineJsonDTO.getProducts().isEmpty()) {
					if(timelineJsonDTOMap.getProducts() != null) {
						timelineJsonDTOMap.getProducts().addAll(timelineJsonDTO.getProducts());
					}
					else {
						timelineJsonDTOMap.setProducts(timelineJsonDTO.getProducts());
					}
				}
				
				mapTimelineJsonDTO.put(key, timelineJsonDTOMap);
				
			}
			else{
				mapTimelineJsonDTO.put(key, timelineJsonDTO);
			}
			
		}
		
		if(!mapTimelineJsonDTO.values().isEmpty()){
			groupListTimelineJsonDTO.addAll(new ArrayList<>(mapTimelineJsonDTO.values()));
		}
		
		return groupListTimelineJsonDTO;
		
	}
	
	private List<TimelineJsonDTO> orderByTimestampDesc(List<TimelineJsonDTO> listTimelineJsonDTO) {
		
		Collections.sort(listTimelineJsonDTO, new Comparator<TimelineJsonDTO>() {
		    @Override
		    public int compare(TimelineJsonDTO t1, TimelineJsonDTO t2) {
		    	if(t1.getTimestamp().before(t2.getTimestamp())){
		    		return 1;
		        }
		    	else if(t1.getTimestamp().after(t2.getTimestamp())){
		    		return -1;
		        }
		    	else{
		            return 0;
		        }
		    }
		});
		
		return listTimelineJsonDTO;
		
	}
	
}
