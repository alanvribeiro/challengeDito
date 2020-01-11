package com.dito.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dito.challenge.model.Events;

public interface EventsRepository extends JpaRepository<Events, Long> {
	
	@Query("SELECT event FROM Events WHERE event LIKE %:keyword%")
	public List<String> search(@Param("keyword") String keyword);

}
