package com.web.ecommerce.domain.event.service;

import com.web.ecommerce.domain.event.dto.request.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.CreateEventRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest.UpdateEventFieldRequest;
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
  @Transactional
  public EventResponse createEvent(CreateEventRequest request) {
    if (eventRepository.existsByEventKey(request.getEventKey())) {
      throw new CustomException(EventErrorCode.DUPLICATE_EVENT_NAME);
    }

    Event event = Event.builder()
        .eventKey(request.getEventKey())
        .eventName(request.getEventName())
        .description(request.getDescription())
        .isActive(request.getIsActive())
        .build();

    eventRepository.save(event);

    return eventMapper.toEventResponse(event, List.of());
  }

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
  public EventResponse updateEvent(Long eventId, UpdateEventRequest request) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    event.update(request.getEventKey(), request.getEventName(), request.getDescription(), request.getIsActive());

    if (request.getFields() != null) {
      List<EventField> updatedFields = request.getFields().stream()
          .map(f -> {
            if (f.getFieldId() == null) {
              return EventField.builder()
                  .event(event)
                  .fieldName(f.getFieldName())
                  .fieldType(f.getFieldType())
                  .isRequired(f.getIsRequired())
                  .description(f.getDescription())
                  .build();
            }
            EventField field = eventFieldRepository.findById(f.getFieldId())
                .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_FIELD_NOT_FOUND));
            field.update(f.getFieldName(), f.getFieldType(), f.getIsRequired(), f.getDescription());
            return field;
          })
          .toList();
      eventFieldRepository.saveAll(updatedFields);
    }

    List<EventField> fields = eventFieldRepository.findByEventId(eventId);
    return eventMapper.toEventResponse(event, fields);
  }

  @Override
  @Transactional
  public void deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new CustomException(EventErrorCode.EVENT_NOT_FOUND));

    List<EventField> fields = eventFieldRepository.findByEventId(eventId);
    eventFieldRepository.deleteAll(fields);
    eventRepository.delete(event);
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
