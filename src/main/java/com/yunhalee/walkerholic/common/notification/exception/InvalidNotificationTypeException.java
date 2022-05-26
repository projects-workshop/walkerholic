package com.yunhalee.walkerholic.common.notification.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class InvalidNotificationTypeException extends InvalidValueException {

    public InvalidNotificationTypeException(String message) {
        super(message);
    }

    public InvalidNotificationTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNotificationTypeException(Throwable cause) {
        super(cause);
    }
}
