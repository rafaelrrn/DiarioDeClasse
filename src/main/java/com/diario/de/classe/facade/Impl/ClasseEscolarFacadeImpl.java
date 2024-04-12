package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ClasseEscolarFacade;
import com.diario.de.classe.model.ClasseEscolar;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.ClasseEscolarService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class ClasseEscolarFacadeImpl implements ClasseEscolarFacade {
    final private static Logger LOG = LogManager.getLogger(ClasseEscolarFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    ClasseEscolarService classeEscolarService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasClasses() {
        List<ClasseEscolar> classeList = classeEscolarService.buscarTodasClasses();
        return defaultHandleResponse.responseHandler(classeList, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarClassePorCodCls(Long codCls) {
        ClasseEscolar classeEscolar = classeEscolarService.buscarClassePorCodCls(codCls);
        return defaultHandleResponse.responseHandler(classeEscolar, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarClasse(String classe) {
        ClasseEscolar classeCriada = classeEscolarService.criarClasse(classe);
        return defaultHandleResponse.responseHandler(classeCriada, HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarClasse(Long codCls, String classeEscolarBody) {
        ClasseEscolar classeEscolarDoBanco = classeEscolarService.buscarClassePorCodCls(codCls);
        if(!ObjectUtils.isEmpty(classeEscolarDoBanco)) classeEscolarService.atualizarClasse(codCls, classeEscolarBody, classeEscolarDoBanco);
        return defaultHandleResponse.responseHandler(classeEscolarDoBanco, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarClasse(Long codCls) {
        ClasseEscolar classeEscolar = classeEscolarService.buscarClassePorCodCls(codCls);
        if(!ObjectUtils.isEmpty(classeEscolar)) classeEscolarService.deletarClasse(classeEscolar);
        return defaultHandleResponse.responseHandler(classeEscolar, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
