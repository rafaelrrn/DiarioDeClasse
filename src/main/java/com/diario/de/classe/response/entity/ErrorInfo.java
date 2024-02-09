package com.diario.de.classe.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorInfo {
    private String message;
    private List<String> developerMessage;
    private String url;
    private Integer code;
    private HttpStatus status;
    private String info;
    private List<ErrorItem> errors = null;

    public ErrorInfo(String url, String message) {
        this.url = url;
        this.message = message;
    }

    public ErrorInfo(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ErrorInfo(String url, String message, List<String> developerMessage){
        this.url = url;
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public ErrorInfo(String userMessage, List<String> developerMessage, String url) {
        this.message = userMessage;
        this.developerMessage = developerMessage;
        this.url = url;
    }

    public ErrorInfo(String message, List<String> developerMessage){
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public ErrorInfo(String message, String info, List<String> developerMessage, HttpStatus status, String url) {
        this.message = message;
        this.info = info;
        this.developerMessage = developerMessage;
        this.status = status;
        this.url = url;
    }
}