package com.hcmus.chatdiary.exception;


import com.google.api.client.http.HttpStatusCodes;

public class BadRequestException extends ApplicationRuntimeException {

    public BadRequestException() {
        super(HttpStatusCodes.STATUS_CODE_BAD_REQUEST);
    }

    public BadRequestException(String code) {
        super(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, code);
    }

    public BadRequestException(String code, Object data) {
        super(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, code, data);
    }
}
