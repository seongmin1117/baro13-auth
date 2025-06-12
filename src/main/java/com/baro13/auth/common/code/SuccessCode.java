package com.baro13.auth.common.code;

import lombok.Getter;

@Getter
public enum SuccessCode {

    OK(200, "성공적으로 처리되었습니다."),
    CREATED(201, "성공적으로 생성되었습니다."),
    NO_CONTENT(204, "성공적으로 처리되었습니다.");

    private final Integer code;
    private final String message;

    SuccessCode(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }
}
