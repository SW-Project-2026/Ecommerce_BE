package com.web.ecommerce.domain.user.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND("USER001", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER002", "이미 사용 중인 아이디입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("USER003", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("USER004", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_CONFIRM_MISMATCH("USER005", "비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_INACTIVE("USER006", "탈퇴하거나 정지된 계정입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("USER007", "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("USER008", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("USER009", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("USER010", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
