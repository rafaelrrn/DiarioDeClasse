package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.PeriodoDTO;
import com.diario.de.classe.facade.PeriodoFacade;
import com.diario.de.classe.model.Mes;
import com.diario.de.classe.model.Periodo;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.PeriodoService;
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
public class PeriodoFacadeImpl implements PeriodoFacade {
    final private static Logger LOG = LogManager.getLogger(PeriodoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    PeriodoService periodoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosPeriodos() {
        List<Periodo> entities = periodoService.buscarTodosPeriodos();
        List<PeriodoDTO> dtos = entities.stream().map(PeriodoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarPeriodoPoridPeriodo(Long idPeriodo) {
        Periodo entity = periodoService.buscarPeriodoPoridPeriodo(idPeriodo);
        PeriodoDTO dto = entity != null ? new PeriodoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarPeriodo(PeriodoDTO periodoDTO) {
        Periodo entity = new Periodo();
        BeanUtils.copyProperties(periodoDTO, entity);
        Periodo salvo = periodoService.criarPeriodo(entity);
        return defaultHandleResponse.responseHandler(new PeriodoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarPeriodo(Long idPeriodo, PeriodoDTO periodoDTO) {
        Periodo entity = periodoService.buscarPeriodoPoridPeriodo(idPeriodo);
        if(!ObjectUtils.isEmpty(entity)) {
            Periodo atualizado = new Periodo();
            BeanUtils.copyProperties(periodoDTO, atualizado);
            periodoService.atualizarPeriodo(idPeriodo, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new PeriodoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarPeriodo(Long idPeriodo) {
        Periodo periodo = periodoService.buscarPeriodoPoridPeriodo(idPeriodo);
        if(!ObjectUtils.isEmpty(periodo)) periodoService.deletarPeriodo(periodo);
        return defaultHandleResponse.responseHandler(periodo, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
