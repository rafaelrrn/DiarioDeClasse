package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.MesDTO;
import com.diario.de.classe.facade.MesFacade;
import com.diario.de.classe.model.InstituicaoEnsino;
import com.diario.de.classe.model.Mes;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.MesService;
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
public class MesFacadeImpl implements MesFacade {
    final private static Logger LOG = LogManager.getLogger(MesFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private MesService mesService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosMeses() {
        List<Mes> entities = mesService.buscarTodosMeses();
        List<MesDTO> dtos = entities.stream().map(MesDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarMesPoridMes(Long idMes) {
        Mes entity = mesService.buscarMesPoridMes(idMes);
        MesDTO dto = entity != null ? new MesDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarMes(MesDTO mesDTO) {
        Mes entity = new Mes();
        BeanUtils.copyProperties(mesDTO, entity);
        Mes salvo = mesService.criarMes(entity);
        return defaultHandleResponse.responseHandler(new MesDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarMes(Long idMes, MesDTO mesDTO) {
        Mes entity = mesService.buscarMesPoridMes(idMes);
        if(!ObjectUtils.isEmpty(entity)) {
            Mes atualizado = new Mes();
            BeanUtils.copyProperties(mesDTO, atualizado);
            mesService.atualizarMes(idMes, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new MesDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarMes(Long idMes) {
        Mes mes = mesService.buscarMesPoridMes(idMes);
        if(!ObjectUtils.isEmpty(mes)) mesService.deletarMes(mes);
        return defaultHandleResponse.responseHandler(mes, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
