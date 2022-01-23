package com.yunhalee.walkerholic.review.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class ReviewNotFoundException extends EntityNotFoundException {

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewNotFoundException(Throwable cause) {
        super(cause);
    }
}
