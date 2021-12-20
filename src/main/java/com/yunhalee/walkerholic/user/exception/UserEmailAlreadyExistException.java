package com.yunhalee.walkerholic.user.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class UserEmailAlreadyExistException extends InvalidValueException {

    public UserEmailAlreadyExistException(String message) {
        super(message);
    }

    public UserEmailAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
