package com.vietbank.vietbank_digital.config.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(
                key,
                args,
                "Default message",
                LocaleContextHolder.getLocale());
    }

    // Handle custom BusinessException
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException exception) {
        String message = getMessage(exception.getMessageKey(), exception.getArgs());
        String detailMessage = exception.getLocalizedMessage();
        int code = getCodeFromHttpStatus(exception.getHttpStatus());
        String moreInformation = "http://localhost:8080/api/v1/exception/" + code;

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    // Handle ResourceNotFoundException
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return handleBusinessException(exception);
    }

    // Handle DuplicateResourceException
    @ExceptionHandler({DuplicateResourceException.class})
    public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException exception) {
        return handleBusinessException(exception);
    }

    // Handle TransactionFailedException
    @ExceptionHandler({TransactionFailedException.class})
    public ResponseEntity<Object> handleTransactionFailedException(TransactionFailedException exception) {
        return handleBusinessException(exception);
    }

    // Handle UnauthorizedException
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException exception) {
        return handleBusinessException(exception);
    }

    // Handle InsufficientBalanceException
    @ExceptionHandler({InsufficientBalanceException.class})
    public ResponseEntity<Object> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        return handleBusinessException(exception);
    }

    // Default exception
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception) {
        String message = getMessage("Exception.message");
        String detailMessage = exception.getLocalizedMessage();
        int code = 1;
        String moreInformation = "http://localhost:8080/api/v1/exception/1";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Not found url handler
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = getMessage("NoHandlerFoundException.message")
                + exception.getHttpMethod() + " " + exception.getRequestURL();
        String detailMessage = exception.getLocalizedMessage();
        int code = 2;
        String moreInformation = "http://localhost:8080/api/v1/exception/2";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, status);
    }

    // Not support HTTP Method
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = getMessageFromHttpRequestMethodNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        int code = 3;
        String moreInformation = "http://localhost:8080/api/v1/exception/3";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, status);
    }

    private String getMessageFromHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        String message = exception.getMethod() + " " + getMessage("HttpRequestMethodNotSupportedException.message");
        for (HttpMethod method : exception.getSupportedHttpMethods()) {
            message += method + " ";
        }
        return message;
    }

    // Not support media type
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
                                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = getMessageFromHttpMediaTypeNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        int code = 4;
        String moreInformation = "http://localhost:8080/api/v1/exception/4";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, status);
    }

    private String getMessageFromHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        String message = exception.getContentType() + " " + getMessage("HttpMediaTypeNotSupportedException.message");
        for (MediaType method : exception.getSupportedMediaTypes()) {
            message += method + ", ";
        }
        return message.substring(0, message.length() - 2);
    }

    // MethodArgumentNotValidException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = getMessage("MethodArgumentNotValidException.message");
        String detailMessage = exception.getLocalizedMessage();

        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        int code = 5;
        String moreInformation = "http://localhost:8080/api/v1/exception/5";

        ErrorResponse response = new ErrorResponse(message, detailMessage, errors, code, moreInformation);

        return new ResponseEntity<>(response, status);
    }

    // MissingServletRequestParameterException
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        String message = exception.getParameterName() + " " + getMessage("MissingServletRequestParameterException.message");
        String detailMessage = exception.getLocalizedMessage();
        int code = 6;
        String moreInformation = "http://localhost:8080/api/v1/exception/6";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, status);
    }

    // MethodArgumentTypeMismatchException
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {

        String message = exception.getName() + " " + getMessage("MethodArgumentTypeMismatchException.message")
                + exception.getRequiredType().getName();
        String detailMessage = exception.getLocalizedMessage();
        int code = 7;
        String moreInformation = "http://localhost:8080/api/v1/exception/7";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Helper method to get code from HttpStatus
    private int getCodeFromHttpStatus(HttpStatus status) {
        switch (status) {
            case NOT_FOUND: return 404;
            case CONFLICT: return 409;
            case FORBIDDEN: return 403;
            case BAD_REQUEST: return 400;
            default: return status.value();
        }
    }

}
