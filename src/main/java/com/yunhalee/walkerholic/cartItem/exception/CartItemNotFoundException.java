package com.yunhalee.walkerholic.cartItem.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class CartItemNotFoundException extends EntityNotFoundException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
