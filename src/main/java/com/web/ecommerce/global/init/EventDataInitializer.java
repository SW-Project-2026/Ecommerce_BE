package com.web.ecommerce.global.init;

import com.web.ecommerce.domain.event.entity.Event;
import com.web.ecommerce.domain.event.entity.EventField;
import com.web.ecommerce.domain.event.enums.FieldType;
import com.web.ecommerce.domain.event.repository.EventFieldRepository;
import com.web.ecommerce.domain.event.repository.EventRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDataInitializer {

  private final EventRepository eventRepository;
  private final EventFieldRepository eventFieldRepository;

  @Transactional
  public void syncIfEmpty() {
    if (eventRepository.count() > 0) {
      log.info("이벤트 데이터 이미 존재 - 초기화 건너뜀");
      return;
    }
    log.info("이벤트 초기 데이터 삽입 시작");
    initEvents();
    log.info("이벤트 초기 데이터 삽입 완료");
  }

  private void initEvents() {
    // 1. 구매버튼 클릭
    Event purchaseClick = saveEvent("purchase_button_click",
        "사용자가 상품 구매 버튼을 클릭했을 때 발생하는 이벤트", true);
    saveFields(purchaseClick,
        field("approvedAmount", FieldType.NUMBER, true, "결제 승인된 최종 금액 (원 단위)"),
        field("productName", FieldType.STRING, true, "구매한 상품의 이름"),
        field("productId", FieldType.STRING, true, "상품 고유 식별자"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 2. 찜/장바구니 추가·삭제
    Event cartAction = saveEvent("wishlist_cart_action",
        "사용자가 상품을 찜하거나 장바구니에 추가 또는 삭제할 때 발생하는 이벤트", true);
    saveFields(cartAction,
        field("productName", FieldType.STRING, true, "찜/장바구니 대상 상품명"),
        field("productId", FieldType.STRING, true, "상품 고유 식별자"),
        field("actionType", FieldType.STRING, true, "add / remove 구분값"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 3. 쿠폰 수령
    Event couponReceived = saveEvent("coupon_received",
        "사용자가 쿠폰을 발급받을 때 발생하는 이벤트", true);
    saveFields(couponReceived,
        field("couponCode", FieldType.STRING, true, "발급된 쿠폰의 고유 코드"),
        field("discountAmount", FieldType.NUMBER, true, "쿠폰 적용 시 할인되는 금액 (원 단위)"),
        field("expiryDate", FieldType.DATETIME, false, "쿠폰 유효 만료 날짜 — 만료율 분석에 유용"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 4. 쿠폰 사용
    Event couponUsed = saveEvent("coupon_used",
        "사용자가 결제 시 쿠폰을 적용할 때 발생하는 이벤트", true);
    saveFields(couponUsed,
        field("couponCode", FieldType.STRING, true, "사용된 쿠폰의 고유 코드"),
        field("discountAmount", FieldType.NUMBER, true, "실제 적용된 할인 금액 (원 단위)"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 5. 광고 클릭
    Event adClick = saveEvent("ad_click",
        "사용자가 광고 배너 또는 광고 상품을 클릭할 때 발생하는 이벤트", true);
    saveFields(adClick,
        field("productName", FieldType.STRING, true, "광고된 상품명"),
        field("productId", FieldType.STRING, true, "광고 상품 고유 식별자"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 6. 검색 버튼 클릭
    Event searchClick = saveEvent("search_button_click",
        "사용자가 검색어를 입력하고 검색을 실행할 때 발생하는 이벤트", true);
    saveFields(searchClick,
        field("searchKeyword", FieldType.STRING, true, "사용자가 입력한 검색어"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 7. 페이지 진입
    Event pageView = saveEvent("page_view",
        "사용자가 특정 페이지에 진입할 때 발생하는 이벤트", true);
    saveFields(pageView,
        field("pageName", FieldType.STRING, true, "진입한 페이지의 이름 또는 경로"),
        field("dwellTime", FieldType.NUMBER, true, "페이지 체류시간 (초 단위)"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 8. 제품 상세 페이지 진입
    Event productDetailView = saveEvent("product_detail_view",
        "사용자가 특정 상품의 상세 페이지로 이동할 때 발생하는 이벤트", true);
    saveFields(productDetailView,
        field("productName", FieldType.STRING, true, "조회한 상품명"),
        field("productId", FieldType.STRING, true, "상품 고유 식별자"),
        field("dwellTime", FieldType.NUMBER, true, "상세 페이지 체류시간 (초 단위)"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 9. 리뷰 등록 버튼 클릭
    Event reviewSubmit = saveEvent("review_submit_click",
        "사용자가 리뷰 작성 후 등록 버튼을 클릭할 때 발생하는 이벤트", true);
    saveFields(reviewSubmit,
        field("reviewRating", FieldType.NUMBER, true, "사용자가 부여한 별점 (1~5 정수)"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 10. 포인트 적립
    Event pointEarned = saveEvent("point_earned",
        "사용자에게 포인트가 적립될 때 발생하는 이벤트", true);
    saveFields(pointEarned,
        field("earnedPoints", FieldType.NUMBER, true, "이번에 적립된 포인트 수"),
        field("earnReason", FieldType.STRING, true, "적립 원인 (구매완료 / 리뷰작성 / 출석 등)"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );

    // 11. 포인트 사용 (추후 개발 - isActive: false)
    Event pointUsed = saveEvent("point_used",
        "사용자가 결제 시 포인트를 차감하여 사용할 때 발생하는 이벤트", false);
    saveFields(pointUsed,
        field("usedPoints", FieldType.NUMBER, true, "이번 거래에서 사용된 포인트 수"),
        field("eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
    );
  }

  private Event saveEvent(String eventName, String description, Boolean isActive) {
    return eventRepository.save(Event.builder()
        .eventName(eventName)
        .description(description)
        .isActive(isActive)
        .build());
  }

  private void saveFields(Event event, EventField... fields) {
    List<EventField> fieldList = Arrays.stream(fields)
        .map(f -> EventField.builder()
            .event(event)
            .fieldName(f.getFieldName())
            .fieldType(f.getFieldType())
            .isRequired(f.getIsRequired())
            .description(f.getDescription())
            .build())
        .toList();
    eventFieldRepository.saveAll(fieldList);
  }

  private EventField field(String fieldName, FieldType fieldType, boolean isRequired, String description) {
    return EventField.builder()
        .fieldName(fieldName)
        .fieldType(fieldType)
        .isRequired(isRequired)
        .description(description)
        .build();
  }
}
