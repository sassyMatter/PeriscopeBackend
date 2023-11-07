package com.app.models.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatus;


@Getter
@Setter
@Builder
public class MetaDataResponse<T> {
    private HttpStatus httpStatus;
    private String messageCode;
    private T data;
    private String errorCode;
    private String errorMessage;

    public MetaDataResponse() {
        // Default constructor
    }

    // Constructors with and without error information
    public MetaDataResponse(HttpStatus httpStatus, String messageCode, T data) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.data = data;
    }

    public MetaDataResponse(HttpStatus httpStatus, String messageCode, T data, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters and setters for all fields
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
