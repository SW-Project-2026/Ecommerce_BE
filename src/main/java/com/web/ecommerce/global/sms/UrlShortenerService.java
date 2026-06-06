package com.web.ecommerce.global.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class UrlShortenerService {

    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");
    private final RestClient restClient = RestClient.create();

    public String shortenUrlsInText(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String shortened = shorten(matcher.group());
            matcher.appendReplacement(sb, Matcher.quoteReplacement(shortened));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String shorten(String url) {
        try {
            String result = restClient.get()
                    .uri("https://tinyurl.com/api-create.php?url=" + url)
                    .retrieve()
                    .body(String.class);
            return (result != null && result.startsWith("https://tinyurl.com")) ? result : url;
        } catch (Exception e) {
            log.warn("URL 단축 실패, 원본 사용: {}", url);
            return url;
        }
    }
}
