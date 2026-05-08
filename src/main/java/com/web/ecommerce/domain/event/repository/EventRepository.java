package com.web.ecommerce.domain.event.repository;

import com.web.ecommerce.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

  boolean existsByEventName(String eventName);
}
