package com.yunhalee.walkerholic.order.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class OrderAlreadyDeliveredException extends InvalidValueException {

    public OrderAlreadyDeliveredException(String message) {
        super(message);
    }
}
