package com.web.ecommerce.global.page.mapper;

import com.web.ecommerce.global.page.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

  public <T> PageResponse<T> toPageResponse(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .pageNum(page.getNumber())
        .pageSize(page.getSize())
        .last(page.isLast())
        .first(page.isFirst())
        .build();
  }
}
