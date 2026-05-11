package com.web.ecommerce.domain.event.service;

import com.web.ecommerce.domain.event.dto.request.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import java.util.List;

public interface EventService {

  List<EventResponse> getEvents(Boolean isActive);

  EventFieldResponse addEventField(Long eventId, CreateEventFieldRequest request);

  EventFieldResponse updateEventField(Long eventId, Long fieldId, UpdateEventFieldRequest request);

  void deleteEventField(Long eventId, Long fieldId);
}
