package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.GrauDTO;
import com.diario.de.classe.facade.GrauFacade;
import com.diario.de.classe.model.Grau;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.GrauService;
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
public class GrauFacadeImpl implements GrauFacade {
    final private static Logger LOG = LogManager.getLogger(GrauFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    GrauService grauService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosGraus() {
        List<Grau> entities = grauService.buscarTodosGraus();
        List<GrauDTO> dtos = entities.stream().map(GrauDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarGrauPoridGrau(Long idGrau) {
        Grau entity = grauService.buscarGrauPoridGrau(idGrau);
        GrauDTO dto = entity != null ? new GrauDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarGrau(GrauDTO grauDTO) {
        Grau entity = new Grau();
        BeanUtils.copyProperties(grauDTO, entity);
        Grau salvo = grauService.criarGrau(entity);
        return defaultHandleResponse.responseHandler(new GrauDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarGrau(Long idGrau, GrauDTO grauDTO) {
        Grau entity = grauService.buscarGrauPoridGrau(idGrau);
        if(!ObjectUtils.isEmpty(entity)) {
            Grau atualizado = new Grau();
            BeanUtils.copyProperties(grauDTO, atualizado);
            grauService.atualizarGrau(idGrau, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new GrauDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarGrau(Long idGrau) {
        Grau grau = grauService.buscarGrauPoridGrau(idGrau);
        if(!ObjectUtils.isEmpty(grau)) grauService.deletarGrau(grau);
        return defaultHandleResponse.responseHandler(grau, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
