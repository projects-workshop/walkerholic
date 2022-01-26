package com.yunhalee.walkerholic.review.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class InvalidRatingException extends InvalidValueException {

    public InvalidRatingException(String message) {
        super(message);
    }
}
