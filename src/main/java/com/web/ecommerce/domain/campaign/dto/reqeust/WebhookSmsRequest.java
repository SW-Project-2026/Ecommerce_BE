package com.web.ecommerce.domain.campaign.dto.reqeust;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebhookSmsRequest {
    private List<Long> userIds;
    private String source; // REALTIME | BATCH
}
