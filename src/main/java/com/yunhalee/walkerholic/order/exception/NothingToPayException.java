package com.yunhalee.walkerholic.order.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class NothingToPayException extends InvalidValueException {

    public NothingToPayException(String message) {
        super(message);
    }
}
