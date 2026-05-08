package com.web.ecommerce.domain.event.mapper;

import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import com.web.ecommerce.domain.event.entity.Event;
import com.web.ecommerce.domain.event.entity.EventField;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

  public EventResponse toEventResponse(Event event, List<EventField> fields) {
    return EventResponse.builder()
        .eventId(event.getId())
        .eventName(event.getEventName())
        .description(event.getDescription())
        .isActive(event.getIsActive())
        .createdAt(event.getCreatedAt().toString())
        .fields(fields.stream().map(this::toEventFieldResponse).toList())
        .build();
  }

  public EventFieldResponse toEventFieldResponse(EventField field) {
    return EventFieldResponse.builder()
        .fieldId(field.getId())
        .fieldName(field.getFieldName())
        .fieldType(field.getFieldType().name())
        .isRequired(field.getIsRequired())
        .description(field.getDescription())
        .build();
  }
}
