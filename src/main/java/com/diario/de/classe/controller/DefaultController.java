package com.diario.de.classe.controller;

import com.diario.de.classe.response.ResponseDiarioDeClasse;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultController {
    public long time;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;

    @Autowired
    public DefaultController() {
        time = System.currentTimeMillis();
    }
}
