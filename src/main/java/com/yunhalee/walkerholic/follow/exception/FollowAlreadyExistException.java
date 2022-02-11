package com.yunhalee.walkerholic.follow.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class FollowAlreadyExistException extends InvalidValueException {

    public FollowAlreadyExistException(String message) {
        super(message);
    }
}
