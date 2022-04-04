package com.yunhalee.walkerholic.order.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class OrderDuplicated extends InvalidValueException {

    public OrderDuplicated(String message) {
        super(message);
    }
}
