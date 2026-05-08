package com.web.ecommerce.domain.event.repository;

import com.web.ecommerce.domain.event.entity.EventField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventFieldRepository extends JpaRepository<EventField, Long> {

  @Query("SELECT ef FROM EventField ef JOIN ef.event e WHERE e.eventName = :eventName AND ef.fieldName = :fieldName")
  Optional<EventField> findByEventNameAndFieldName(@Param("eventName") String eventName,
      @Param("fieldName") String fieldName);

  List<EventField> findByEventId(Long eventId);

  boolean existsByEventIdAndFieldName(Long eventId, String fieldName);
}
