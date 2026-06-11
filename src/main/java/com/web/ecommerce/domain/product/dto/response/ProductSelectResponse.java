package com.web.ecommerce.domain.product.dto.response;

import com.web.ecommerce.domain.product.entity.Product;
import lombok.Builder;

@Builder
public record ProductSelectResponse(
    Long productId,
    String name,
    String imageUrl
) {
  public static ProductSelectResponse from(Product product) {
    return ProductSelectResponse.builder()
        .productId(product.getProductId())
        .name(product.getName())
        .imageUrl(product.getImageUrl())
        .build();
  }
}
