package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.EnsinoDTO;
import com.diario.de.classe.facade.EnsinoFacade;
import com.diario.de.classe.model.Ensino;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.EnsinoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
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
public class EnsinoFacadeImpl implements EnsinoFacade {
    final private static Logger LOG = LogManager.getLogger(EnsinoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private EnsinoService ensinoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosEnsinos() {
        List<Ensino> entities = ensinoService.buscarTodosEnsinos();
        List<EnsinoDTO> dtos = entities.stream().map(EnsinoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarEnsinoPoridEnsino(Long idEnsino) {
        Ensino entity = ensinoService.buscarEnsinoPoridEnsino(idEnsino);
        EnsinoDTO dto = entity != null ? new EnsinoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarEnsino(EnsinoDTO ensinoDTO) {
        Ensino entity = new Ensino();
        BeanUtils.copyProperties(ensinoDTO, entity);
        Ensino salvo = ensinoService.criarEnsino(entity);
        return defaultHandleResponse.responseHandler(new EnsinoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarEnsino(Long idEnsino, EnsinoDTO ensinoDTO) {
        Ensino entity = ensinoService.buscarEnsinoPoridEnsino(idEnsino);
        if(!ObjectUtils.isEmpty(entity)) {
            Ensino atualizado = new Ensino();
            BeanUtils.copyProperties(ensinoDTO, atualizado);
            ensinoService.atualizarEnsino(idEnsino, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new EnsinoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarEnsino(Long idEnsino) {
        Ensino ensino = ensinoService.buscarEnsinoPoridEnsino(idEnsino);
        if(!ObjectUtils.isEmpty(ensino)) ensinoService.deletarEnsino(ensino);
        return defaultHandleResponse.responseHandler(ensino, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
