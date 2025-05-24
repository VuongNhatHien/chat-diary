package com.hcmus.exception;


import com.google.api.client.http.HttpStatusCodes;

public class UnauthorizedException extends ApplicationRuntimeException {

    public UnauthorizedException() {
        super(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED);
    }

    public UnauthorizedException(String code) {
        super(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED, code);
    }

    public UnauthorizedException(String code, Object data) {
        super(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED, code, data);
    }
}
