package com.web.ecommerce.domain.event.controller;

import com.web.ecommerce.domain.event.dto.request.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import com.web.ecommerce.domain.event.service.EventService;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

  private final EventService eventService;

  @Override
  @GetMapping
  public ResponseEntity<BaseResponse<List<EventResponse>>> getEvents(@RequestParam(required = false) Boolean isActive) {
    return ResponseEntity.ok(BaseResponse.success(eventService.getEvents(isActive)));
  }

  @Override
  @PostMapping("/{eventId}/fields")
  public ResponseEntity<BaseResponse<EventFieldResponse>> createEventField(@PathVariable Long eventId,
      @RequestBody CreateEventFieldRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "이벤트 필드가 추가되었습니다.", eventService.addEventField(eventId, request)));
  }

  @Override
  @PatchMapping("/{eventId}/fields/{fieldId}")
  public ResponseEntity<BaseResponse<EventFieldResponse>> updateEventField(@PathVariable Long eventId,
      @PathVariable Long fieldId, @RequestBody UpdateEventFieldRequest request) {
    return ResponseEntity.ok(BaseResponse.success(eventService.updateEventField(eventId, fieldId, request)));
  }

  @Override
  @DeleteMapping("/{eventId}/fields/{fieldId}")
  public ResponseEntity<Void> deleteEventField(@PathVariable Long eventId, @PathVariable Long fieldId) {
    eventService.deleteEventField(eventId, fieldId);
    return ResponseEntity.noContent().build();
  }

}
