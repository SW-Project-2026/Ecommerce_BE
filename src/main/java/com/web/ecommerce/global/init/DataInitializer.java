package com.web.ecommerce.global.init;

import com.web.ecommerce.domain.product.service.ProductSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProductSyncService productSyncService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("서버 시작 - 상품 데이터 초기화 확인");
        productSyncService.syncIfEmpty();
    }
}
