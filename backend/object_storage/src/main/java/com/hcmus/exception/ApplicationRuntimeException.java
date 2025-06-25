package com.hcmus.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationRuntimeException extends RuntimeException {

    private final int status;
    private String code;
    private Object data;

    protected ApplicationRuntimeException(int status) {
        super();
        this.status = status;
    }

    protected ApplicationRuntimeException(int status, String code) {
        super();
        this.status = status;
        this.code = code;
    }

    protected ApplicationRuntimeException(int status, String code, Object data) {
        super();
        this.status = status;
        this.code = code;
        this.data = data;
    }
}
