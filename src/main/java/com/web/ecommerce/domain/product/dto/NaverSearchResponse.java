package com.web.ecommerce.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverSearchResponse {

    private int total;

    private int start;

    private int display;

    private List<NaverProductItem> items;
}
