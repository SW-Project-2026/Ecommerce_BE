package com.web.ecommerce.domain.event.controller;

import com.web.ecommerce.domain.event.dto.request.CreateEventRequest;
import com.web.ecommerce.domain.event.dto.request.CreateEventRequest.CreateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest;
import com.web.ecommerce.domain.event.dto.request.UpdateEventRequest.UpdateEventFieldRequest;
import com.web.ecommerce.domain.event.dto.response.EventResponse;
import com.web.ecommerce.domain.event.dto.response.EventResponse.EventFieldResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Event", description = "이벤트 관리 API")
public interface EventController {

  @Operation(summary = "이벤트 생성")
  ResponseEntity<BaseResponse<EventResponse>> createEvent(@RequestBody CreateEventRequest request);

  @Operation(summary = "이벤트 목록 조회")
  ResponseEntity<BaseResponse<List<EventResponse>>> getEvents(@RequestParam(required = false) Boolean isActive);

  @Operation(summary = "이벤트 단건 조회")
  ResponseEntity<BaseResponse<EventResponse>> getEvent(@PathVariable Long eventId);

  @Operation(summary = "이벤트 수정")
  ResponseEntity<BaseResponse<EventResponse>> updateEvent(@PathVariable Long eventId,
      @RequestBody UpdateEventRequest request);

  @Operation(summary = "이벤트 삭제")
  ResponseEntity<Void> deleteEvent(@PathVariable Long eventId);

  @Operation(summary = "이벤트 필드 추가")
  ResponseEntity<BaseResponse<EventFieldResponse>> addEventField(@PathVariable Long eventId,
      @RequestBody CreateEventFieldRequest request);

  @Operation(summary = "이벤트 필드 수정")
  ResponseEntity<BaseResponse<EventFieldResponse>> updateEventField(@PathVariable Long eventId,
      @PathVariable Long fieldId, @RequestBody UpdateEventFieldRequest request);

  @Operation(summary = "이벤트 필드 삭제")
  ResponseEntity<Void> deleteEventField(@PathVariable Long eventId, @PathVariable Long fieldId);
}
