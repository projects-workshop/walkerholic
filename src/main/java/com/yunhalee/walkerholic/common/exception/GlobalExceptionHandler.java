package com.yunhalee.walkerholic.common.exception;


import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import com.yunhalee.walkerholic.user.exception.UserEmailAlreadyExistException;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserEmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserEmailAlreadyExistException(
        UserEmailAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughStockException(NotEnoughStockException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(OAuthProviderMissMatchException.class)
    public ResponseEntity<ErrorResponse> handleOAuthProviderMissMatchException(
        OAuthProviderMissMatchException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(IllegalAccessException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalAccessException(IllegalAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        HttpStatus status = HttpStatus.NOT_FOUND;
        errorResponse.setStatus(status.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }
}