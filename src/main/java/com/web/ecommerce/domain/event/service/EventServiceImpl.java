package com.web.ecommerce.domain.event.service;

import com.web.ecommerce.domain.event.dto.request.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import com.web.ecommerce.domain.event.entity.Event;
import com.web.ecommerce.domain.event.entity.EventField;
import com.web.ecommerce.domain.event.exception.EventErrorCode;
import com.web.ecommerce.domain.event.mapper.EventMapper;
import com.web.ecommerce.domain.event.repository.EventFieldRepository;
import com.web.ecommerce.domain.event.repository.EventRepository;
import com.web.ecommerce.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final EventFieldRepository eventFieldRepository;
  private final EventMapper eventMapper;


  @Override
  @Transactional(readOnly = true)
  public List<EventResponse> getEvents(Boolean isActive) {
    boolean activeFilter = isActive != null ? isActive : true;
    return eventRepository.findByIsActive(activeFilter).stream()
        .map(e -> eventMapper.toEventResponse(e, eventFieldRepository.findByEventId(e.getId())))
        .toList();
  }

  @Override
  @Transactional
  public EventFieldResponse addEventField(Long eventId, CreateEventFieldRequest request) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    if (eventFieldRepository.existsByEventIdAndFieldName(eventId, request.getFieldName())) {
      throw new CustomException(EventErrorCode.DUPLICATE_FIELD_NAME);
    }

    EventField field = EventField.builder()
        .event(event)
        .fieldName(request.getFieldName())
        .fieldType(request.getFieldType())
        .isRequired(request.getIsRequired())
        .description(request.getDescription())
        .build();

    eventFieldRepository.save(field);
    return eventMapper.toEventFieldResponse(field);
  }

  @Override
  @Transactional
  public EventFieldResponse updateEventField(Long eventId, Long fieldId, UpdateEventFieldRequest request) {
    eventRepository.findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    EventField field = eventFieldRepository.findById(fieldId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_FIELD_NOT_FOUND));

    field.update(request.getFieldName(), request.getFieldType(), request.getIsRequired(), request.getDescription());
    return eventMapper.toEventFieldResponse(field);
  }

  @Override
  @Transactional
  public void deleteEventField(Long eventId, Long fieldId) {
    eventRepository.findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    EventField field = eventFieldRepository.findById(fieldId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_FIELD_NOT_FOUND));

    eventFieldRepository.delete(field);
  }

}
