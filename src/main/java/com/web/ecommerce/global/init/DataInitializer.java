package com.web.ecommerce.global.init;

import com.web.ecommerce.domain.event.entity.Event;
import com.web.ecommerce.domain.event.entity.EventField;
import com.web.ecommerce.domain.event.enums.FieldType;
import com.web.ecommerce.domain.event.repository.EventFieldRepository;
import com.web.ecommerce.domain.event.repository.EventRepository;
import com.web.ecommerce.domain.product.service.ProductSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProductSyncService productSyncService;
    private final EventRepository eventRepository;
    private final EventFieldRepository eventFieldRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("서버 시작 - 데이터 초기화 확인");
        productSyncService.syncIfEmpty();
        seedEventsIfEmpty();
    }

    private void seedEventsIfEmpty() {
        if (eventRepository.count() > 0) {
            log.info("이벤트 데이터가 이미 존재합니다. seed를 건너뜁니다.");
            return;
        }
        log.info("이벤트 데이터 초기 삽입 시작");

        Event purchase      = save("purchase_button_click", "사용자가 상품 구매 버튼을 클릭했을 때 발생하는 이벤트",                   true);
        Event wishlist      = save("wishlist_cart_action",  "사용자가 상품을 찜하거나 장바구니에 추가 또는 삭제할 때 발생하는 이벤트", true);
        Event couponReceive = save("coupon_received",       "사용자가 쿠폰을 발급받을 때 발생하는 이벤트",                             true);
        Event couponUsed    = save("coupon_used",           "사용자가 결제 시 쿠폰을 적용할 때 발생하는 이벤트",                       true);
        Event adClick       = save("ad_click",              "사용자가 광고 배너 또는 광고 상품을 클릭할 때 발생하는 이벤트",           true);
        Event search        = save("search_button_click",   "사용자가 검색어를 입력하고 검색을 실행할 때 발생하는 이벤트",             true);
        Event pageView      = save("page_view",             "사용자가 특정 페이지에 진입할 때 발생하는 이벤트",                        true);
        Event productView   = save("product_detail_view",   "사용자가 특정 상품의 상세 페이지로 이동할 때 발생하는 이벤트",            true);
        Event review        = save("review_submit_click",   "사용자가 리뷰 작성 후 등록 버튼을 클릭할 때 발생하는 이벤트",             true);
        Event pointEarned   = save("point_earned",          "사용자에게 포인트가 적립될 때 발생하는 이벤트",                           true);
        Event pointUsed     = save("point_used",            "사용자가 결제 시 포인트를 차감하여 사용할 때 발생하는 이벤트",            false);

        eventFieldRepository.saveAll(List.of(
            field(purchase, "approvedAmount", FieldType.NUMBER,   true,  "결제 승인된 최종 금액 (원 단위)"),
            field(purchase, "productName",    FieldType.STRING,   true,  "구매한 상품의 이름"),
            field(purchase, "productId",      FieldType.STRING,   true,  "상품 고유 식별자"),
            field(purchase, "eventTimestamp", FieldType.DATETIME, true,  "해당 이벤트 발생 시간"),

            field(wishlist, "productName",    FieldType.STRING,   true,  "찜/장바구니 대상 상품명"),
            field(wishlist, "productId",      FieldType.STRING,   true,  "상품 고유 식별자"),
            field(wishlist, "actionType",     FieldType.STRING,   true,  "add / remove 구분값"),
            field(wishlist, "eventTimestamp", FieldType.DATETIME, true,  "해당 이벤트 발생 시간"),

            field(couponReceive, "couponCode",     FieldType.STRING,   true,  "발급된 쿠폰의 고유 코드"),
            field(couponReceive, "discountAmount", FieldType.NUMBER,   true,  "쿠폰 적용 시 할인되는 금액 (원 단위)"),
            field(couponReceive, "expiryDate",     FieldType.DATE,     false, "쿠폰 유효 만료 날짜 — 만료율 분석에 유용"),
            field(couponReceive, "eventTimestamp", FieldType.DATETIME, true,  "해당 이벤트 발생 시간"),

            field(couponUsed, "couponCode",     FieldType.STRING,   true, "사용된 쿠폰의 고유 코드"),
            field(couponUsed, "discountAmount", FieldType.NUMBER,   true, "실제 적용된 할인 금액 (원 단위)"),
            field(couponUsed, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(adClick, "productName",    FieldType.STRING,   true, "광고된 상품명"),
            field(adClick, "productId",      FieldType.STRING,   true, "광고 상품 고유 식별자"),
            field(adClick, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(search, "searchKeyword",  FieldType.STRING,   true, "사용자가 입력한 검색어"),
            field(search, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(pageView, "pageName",       FieldType.STRING,   true, "진입한 페이지의 이름 또는 경로"),
            field(pageView, "dwellTime",      FieldType.TIME,     true, "페이지 체류시간 (초 단위)"),
            field(pageView, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(productView, "productName",    FieldType.STRING,   true, "조회한 상품명"),
            field(productView, "productId",      FieldType.STRING,   true, "상품 고유 식별자"),
            field(productView, "dwellTime",      FieldType.TIME,     true, "상세 페이지 체류시간 (초 단위)"),
            field(productView, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(review, "reviewRating",   FieldType.NUMBER,   true, "사용자가 부여한 별점 (1~5 정수)"),
            field(review, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(pointEarned, "earnedPoints",   FieldType.NUMBER,   true, "이번에 적립된 포인트 수"),
            field(pointEarned, "earnReason",     FieldType.STRING,   true, "적립 원인 (구매완료 / 리뷰작성 / 출석 등)"),
            field(pointEarned, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간"),

            field(pointUsed, "usedPoints",     FieldType.NUMBER,   true, "이번 거래에서 사용된 포인트 수"),
            field(pointUsed, "eventTimestamp", FieldType.DATETIME, true, "해당 이벤트 발생 시간")
        ));

        log.info("이벤트 데이터 초기 삽입 완료 (이벤트 11개, 필드 34개)");
    }

    private Event save(String name, String description, boolean isActive) {
        return eventRepository.save(Event.builder()
                .eventName(name)
                .description(description)
                .isActive(isActive)
                .build());
    }

    private EventField field(Event event, String fieldName, FieldType fieldType, boolean isRequired, String description) {
        return EventField.builder()
                .event(event)
                .fieldName(fieldName)
                .fieldType(fieldType)
                .isRequired(isRequired)
                .description(description)
                .build();
    }
}
