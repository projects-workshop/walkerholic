package com.yunhalee.walkerholic.cart.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class CartAlreadyExist extends InvalidValueException {

    public CartAlreadyExist(String message) {
        super(message);
    }
}
