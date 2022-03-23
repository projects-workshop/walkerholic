package com.yunhalee.walkerholic.orderitem.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class OrderItemNotFoundException extends EntityNotFoundException {

    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
