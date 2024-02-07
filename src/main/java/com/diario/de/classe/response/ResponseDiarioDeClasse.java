package com.diario.de.classe.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ResponseDiarioDeClasse {
    ResponseEntity<Object> error(String userMessage, HttpStatus status);
    ResponseEntity<Object> error(String userMessage, List<String> developerMessage, HttpStatus status);
    ResponseEntity<Object> error(String userMessage, List<String> developerMessage, HttpStatus status, String url);
    ResponseEntity<Object> success(Object data, long timeStend, HttpStatus status);
    ResponseEntity<Object> success(Object data, HttpStatus status);
}
