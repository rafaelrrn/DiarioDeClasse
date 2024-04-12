package com.diario.de.classe.util;

import com.diario.de.classe.response.ResponseDiarioDeClasse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class DefaultResponseHandleUtil {
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;

    public ResponseEntity<Object> responseHandler(Object result, HttpStatus expectedErrorStatus, String expectedErrorMessage) {
        if (ObjectUtils.isEmpty(result)) return responseDiarioDeClasse.error(expectedErrorMessage, expectedErrorStatus);
        return responseDiarioDeClasse.success(result, HttpStatus.OK);
    }
}
