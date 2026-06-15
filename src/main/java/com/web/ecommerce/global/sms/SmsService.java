package com.web.ecommerce.global.sms;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class SmsService {

    private static final String SOLAPI_URL = "https://api.solapi.com/messages/v4/send";

    private final String apiKey;
    private final String secretKey;
    private final String from;
    private final RestClient restClient;
    private final UrlShortenerService urlShortener;

    public SmsService(
            @Value("${solapi.api-key}") String apiKey,
            @Value("${solapi.secret-key}") String secretKey,
            @Value("${solapi.from}") String from,
            UrlShortenerService urlShortener) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.from = from;
        this.restClient = RestClient.create();
        this.urlShortener = urlShortener;
    }

    public boolean sendCustomMessage(String to, String messageType, String subject, String content) {
        if (to == null || to.isBlank()) {
            return false;
        }
        try {
            String text = content;
            String type;
            if ("LMS".equals(messageType)) {
                type = "LMS";
            } else {
                if (text.getBytes(StandardCharsets.UTF_8).length > 90) {
                    text = urlShortener.shortenUrlsInText(text);
                }
                type = resolveType(text);
            }
            send(to, text, type, subject);
            return true;
        } catch (Exception e) {
            log.error("SMS 발송 실패. to={}", to, e);
            return false;
        }
    }

    public void sendCouponNotification(String to, String couponName, String discountType, int discountAmount) {
        if (to == null || to.isBlank()) {
            return;
        }
        try {
            String text = buildMessage(couponName, discountType, discountAmount);
            String type = resolveType(text);
            send(to, text, type, couponName);
        } catch (Exception e) {
            log.error("SMS 발송 실패. to={}, coupon={}", to, couponName, e);
        }
    }

    private void send(String to, String text, String type, String subject) throws Exception {
        String date = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        String salt = UUID.randomUUID().toString().replace("-", "");
        String signature = sign(date, salt);
        String authorization = String.format("HMAC-SHA256 apiKey=%s, date=%s, salt=%s, signature=%s",
                apiKey, date, salt, signature);

        Object body = "LMS".equals(type)
                ? Map.of("message", Map.of("to", to, "from", from, "text", text, "type", type, "subject", subject))
                : Map.of("message", Map.of("to", to, "from", from, "text", text, "type", type));

        restClient.post()
                .uri(SOLAPI_URL)
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("SMS 발송 완료. to={}, type={}", to, type);
    }

    private String buildMessage(String couponName, String discountType, int discountAmount) {
        String discount = "RATE".equals(discountType)
                ? discountAmount + "% 할인"
                : discountAmount + "원 할인";
        return String.format("[쿠폰 발급 안내]\n%s 쿠폰이 발급되었습니다.\n혜택: %s", couponName, discount);
    }

    private String resolveType(String text) {
        return text.getBytes(StandardCharsets.UTF_8).length <= 90 ? "SMS" : "LMS";
    }

    private String sign(String date, String salt) throws Exception {
        String message = date + salt;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
