package com.hcmus.exception;


import com.google.api.client.http.HttpStatusCodes;

public class NotFoundException extends ApplicationRuntimeException {

    public NotFoundException() {
        super(HttpStatusCodes.STATUS_CODE_NOT_FOUND);
    }

    public NotFoundException(String code) {
        super(HttpStatusCodes.STATUS_CODE_NOT_FOUND, code);
    }

    public NotFoundException(String code, Object data) {
        super(HttpStatusCodes.STATUS_CODE_NOT_FOUND, code, data);
    }
}
