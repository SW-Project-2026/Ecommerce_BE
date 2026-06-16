package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.CollectionType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebhookSmsRequest {
    private List<Long> userIds;
    private CollectionType source;
}
