package com.yunhalee.walkerholic.order.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class OrderNotPaidException extends InvalidValueException {

    public OrderNotPaidException(String message) {
        super(message);
    }
}
