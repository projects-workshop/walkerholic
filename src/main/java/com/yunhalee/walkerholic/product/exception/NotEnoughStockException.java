package com.yunhalee.walkerholic.product.exception;

import com.yunhalee.walkerholic.common.exception.BadRequestException;

public class NotEnoughStockException extends BadRequestException {

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
