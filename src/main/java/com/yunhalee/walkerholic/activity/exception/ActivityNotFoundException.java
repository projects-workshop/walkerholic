package com.yunhalee.walkerholic.activity.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class ActivityNotFoundException extends EntityNotFoundException {

    public ActivityNotFoundException(String message) {
        super(message);
    }

    public ActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityNotFoundException(Throwable cause) {
        super(cause);
    }
}
