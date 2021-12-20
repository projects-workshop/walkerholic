package com.yunhalee.walkerholic.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ErrorResponse {

    private int status;
    private String message;
    private long timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

}