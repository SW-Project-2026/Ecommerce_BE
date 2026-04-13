package com.web.ecommerce.global.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 전역 예외 처리 클래스입니다.
 * develop 브랜치의 Map 기반 에러 처리 로직을 유지합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * CustomException 발생 시 처리합니다.
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<BaseResponse<Object>> handleCustomException(CustomException ex) {
    BaseErrorCode errorCode = ex.getErrorCode();
    log.warn("CustomException 발생: {} - {}", errorCode.getCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getStatus())
            .body(BaseResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
  }

  /**
   * 유효성 검증 실패(MethodArgumentNotValidException) 발생 시 처리합니다.
   * 각 필드별 에러 메시지를 Map에 담아 반환합니다.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Object>> handleValidationException(
      MethodArgumentNotValidException ex) {
    String errorMessages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> String.format("[%s] %s", e.getField(), e.getDefaultMessage()))
            .collect(Collectors.joining(" / "));
    log.warn("Validation 오류 발생: {}", errorMessages);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(BaseResponse.error(400, errorMessages));
  }

  /**
   * 정적 리소스 미존재 시 처리합니다.
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<BaseResponse<Object>> handleNoResourceFound(NoResourceFoundException ex) {
    log.debug("정적 리소스 없음: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(BaseResponse.error(404, "리소스를 찾을 수 없습니다."));
  }

  /**
   * 그 외 모든 예외 발생 시 처리합니다.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Object>> handleException(Exception ex) {
    log.error("Server 오류 발생", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse.error(500, "예상치 못한 서버 오류가 발생했습니다."));
  }
}
