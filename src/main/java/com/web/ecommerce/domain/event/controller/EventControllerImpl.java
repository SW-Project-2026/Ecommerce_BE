package com.web.ecommerce.domain.event.controller;

import com.web.ecommerce.domain.event.dto.request.CreateEventRequest;
import com.web.ecommerce.domain.event.dto.request.CreateEventRequest.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import com.web.ecommerce.domain.event.service.EventService;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

  private final EventService eventService;

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
    return BaseResponse.success(201, "이벤트가 생성되었습니다.", eventService.createEvent(request));
  }

  @Override
  @GetMapping
  public BaseResponse<List<EventResponse>> getEvents() {
    return BaseResponse.success(eventService.getEvents());
  }

  @Override
  @GetMapping("/{eventId}")
  public BaseResponse<EventResponse> getEvent(@PathVariable Long eventId) {
    return BaseResponse.success(eventService.getEvent(eventId));
  }

  @Override
  @PatchMapping("/{eventId}")
  public BaseResponse<EventResponse> updateEvent(@PathVariable Long eventId,
      @RequestBody UpdateEventRequest request) {
    return BaseResponse.success(eventService.updateEvent(eventId, request));
  }

  @Override
  @DeleteMapping("/{eventId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEvent(@PathVariable Long eventId) {
    eventService.deleteEvent(eventId);
  }

  @Override
  @PostMapping("/{eventId}/fields")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<EventFieldResponse> addEventField(@PathVariable Long eventId,
      @RequestBody CreateEventFieldRequest request) {
    return BaseResponse.success(201, "이벤트 필드가 추가되었습니다.", eventService.addEventField(eventId, request));
  }

  @Override
  @PatchMapping("/{eventId}/fields/{fieldId}")
  public BaseResponse<EventFieldResponse> updateEventField(@PathVariable Long eventId,
      @PathVariable Long fieldId, @RequestBody UpdateEventFieldRequest request) {
    return BaseResponse.success(eventService.updateEventField(eventId, fieldId, request));
  }

  @Override
  @DeleteMapping("/{eventId}/fields/{fieldId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEventField(@PathVariable Long eventId, @PathVariable Long fieldId) {
    eventService.deleteEventField(eventId, fieldId);
  }
}
