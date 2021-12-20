package com.yunhalee.walkerholic.security.oauth.exception;

import com.yunhalee.walkerholic.common.exception.InvalidValueException;

public class OAuthProviderMissMatchException extends InvalidValueException {

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }

    public OAuthProviderMissMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthProviderMissMatchException(Throwable cause) {
        super(cause);
    }
}
