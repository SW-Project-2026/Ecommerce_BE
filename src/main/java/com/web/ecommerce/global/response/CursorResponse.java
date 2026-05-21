package com.web.ecommerce.global.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CursorResponse<T> {

  private List<T> content;
  private Long nextCursor;
  private boolean hasNext;

  public static <T> CursorResponse<T> of(List<T> content, Long nextCursor, boolean hasNext) {
    return CursorResponse.<T>builder()
        .content(content)
        .nextCursor(nextCursor)
        .hasNext(hasNext)
        .build();
  }
}
