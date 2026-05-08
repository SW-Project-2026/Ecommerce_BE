package com.web.ecommerce.domain.event.service;

import com.web.ecommerce.domain.event.dto.request.CreateEventRequest;
import com.web.ecommerce.domain.event.dto.request.CreateEventRequest.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import java.util.List;

public interface EventService {

  EventResponse createEvent(CreateEventRequest request);

  List<EventResponse> getEvents(Boolean isActive);

  EventResponse getEvent(Long eventId);

  EventResponse updateEvent(Long eventId, UpdateEventRequest request);

  void deleteEvent(Long eventId);

  EventFieldResponse addEventField(Long eventId, CreateEventFieldRequest request);

  EventFieldResponse updateEventField(Long eventId, Long fieldId, UpdateEventFieldRequest request);

  void deleteEventField(Long eventId, Long fieldId);
}
