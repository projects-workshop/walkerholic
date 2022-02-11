package com.yunhalee.walkerholic.follow.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class CannotFollowOneselfException extends InvalidValueException {

    public CannotFollowOneselfException(String message) {
        super(message);
    }
}
