package com.yunhalee.walkerholic.useractivity.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class UserActivityNotFoundException extends EntityNotFoundException {

    public UserActivityNotFoundException(String message) {
        super(message);
    }

    public UserActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserActivityNotFoundException(Throwable cause) {
        super(cause);
    }
}
