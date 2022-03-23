package com.yunhalee.walkerholic.post.exception;

import com.yunhalee.walkerholic.common.exception.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException(String message) {
        super(message);
    }
}
