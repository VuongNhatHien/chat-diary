package com.hcmus.dto.response;

import com.google.api.client.http.HttpStatusCodes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private T data = null;
    private Integer status = HttpStatusCodes.STATUS_CODE_OK;
    private String errorCode = null;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>();
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> created() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(HttpStatusCodes.STATUS_CODE_CREATED);
        return response;
    }

    public static <T> ApiResponse<T> created(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setStatus(HttpStatusCodes.STATUS_CODE_CREATED);
        return response;
    }

    public static <T> ApiResponse<T> error(Integer status, String code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setErrorCode(code);
        return response;
    }

    public static <T> ApiResponse<T> error(T data, Integer status, String code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setStatus(status);
        response.setErrorCode(code);
        return response;
    }
}
