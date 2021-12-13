package com.yunhalee.walkerholic.user.exception;

public class UserEmailAlreadyExistException extends RuntimeException {

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
