package com.yunhalee.walkerholic.activity.exception;

public class ActivityNotFoundException extends RuntimeException{

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
