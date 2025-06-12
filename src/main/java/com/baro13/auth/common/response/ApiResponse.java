package com.baro13.auth.common.response;

import com.baro13.auth.common.code.SuccessCode;

public record ApiResponse<T>(
    Integer code,
    String message,
    T data
) {
    public static <T> ApiResponse<T> of(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(SuccessCode.OK.getCode(), SuccessCode.OK.getMessage(), data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(SuccessCode.CREATED.getCode(), SuccessCode.CREATED.getMessage(), data);
    }

    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(SuccessCode.NO_CONTENT.getCode(), SuccessCode.NO_CONTENT.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
