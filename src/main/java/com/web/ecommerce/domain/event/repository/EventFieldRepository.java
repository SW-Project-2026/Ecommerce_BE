package com.web.ecommerce.domain.event.repository;

import com.web.ecommerce.domain.event.entity.EventField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFieldRepository extends JpaRepository<EventField, Long> {

  Optional<EventField> findByEventIdAndFieldName(Long eventId, String fieldName);

  List<EventField> findByEventId(Long eventId);

  boolean existsByEventIdAndFieldName(Long eventId, String fieldName);
}
