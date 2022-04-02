package com.yunhalee.walkerholic.cart.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {

    public CartNotFoundException(String message) {
        super(message);
    }
}
