package com.yunhalee.walkerholic.order.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
