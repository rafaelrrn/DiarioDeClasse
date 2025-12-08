package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ComponenteCurricularFacade;
import com.diario.de.classe.model.ComponenteCurricular;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.ComponenteCurricularService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.ComponenteCurricularDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class ComponenteCurricularFacadeImpl implements ComponenteCurricularFacade {
    final private static Logger LOG = LogManager.getLogger(ComponenteCurricularFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    ComponenteCurricularService componenteCurricularService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosComponentesCurriculares() {
        List<ComponenteCurricular> entities = componenteCurricularService.buscarTodosComponentesCurriculares();
        List<ComponenteCurricularDTO> dtos = entities.stream().map(ComponenteCurricularDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarComponenteCurricularPoridComponenteCurricular(Long idComponenteCurricular) {
        ComponenteCurricular entity = componenteCurricularService.buscarComponenteCurricularPoridComponenteCurricular(idComponenteCurricular);
        ComponenteCurricularDTO dto = entity != null ? new ComponenteCurricularDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarComponenteCurricular(ComponenteCurricularDTO componenteCurricularDTO) {
        ComponenteCurricular entity = new ComponenteCurricular();
        BeanUtils.copyProperties(componenteCurricularDTO, entity);
        ComponenteCurricular salvo = componenteCurricularService.criarComponenteCurricular(entity);
        return defaultHandleResponse.responseHandler(new ComponenteCurricularDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarComponenteCurricular(Long idComponenteCurricular, ComponenteCurricularDTO componenteCurricularDTO) {
        ComponenteCurricular entity = componenteCurricularService.buscarComponenteCurricularPoridComponenteCurricular(idComponenteCurricular);
        if(!ObjectUtils.isEmpty(entity)) {
            ComponenteCurricular atualizado = new ComponenteCurricular();
            BeanUtils.copyProperties(componenteCurricularDTO, atualizado);
            componenteCurricularService.atualizarComponenteCurricular(idComponenteCurricular, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new ComponenteCurricularDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarComponenteCurricular(Long idComponenteCurricular) {
        ComponenteCurricular componenteCurricular = componenteCurricularService.buscarComponenteCurricularPoridComponenteCurricular(idComponenteCurricular);
        if(!ObjectUtils.isEmpty(componenteCurricular)) componenteCurricularService.deletarComponenteCurricular(componenteCurricular);
        return defaultHandleResponse.responseHandler(componenteCurricular, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
