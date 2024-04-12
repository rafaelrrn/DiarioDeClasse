package com.diario.de.classe.util;

import com.diario.de.classe.response.ResponseDiarioDeClasse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Throwables.getStackTraceAsString;

@RestControllerAdvice
public class GlobalExceptionHandlerUtil {
    final private static Logger LOG = LogManager.getLogger(GlobalExceptionHandlerUtil.class);
    private String NUMBER_VIOLATION_EXCEPTION = "Violação de tipo de dado. Deve ser numérico.";

    @Autowired
    ResponseDiarioDeClasse responseContabilOnline;

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        List<String> constraintViolationsList = new ArrayList<>();
        constraintViolationsList.add(ex.getMessage());
        LOG.error(getStackTraceAsString(ex));
        return responseContabilOnline.error(
                NUMBER_VIOLATION_EXCEPTION,
                constraintViolationsList,
                HttpStatus.BAD_REQUEST);
    }
}
