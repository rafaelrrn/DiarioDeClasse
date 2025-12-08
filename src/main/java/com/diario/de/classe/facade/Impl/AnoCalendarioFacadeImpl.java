package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.AnoCalendarioDTO;
import com.diario.de.classe.facade.AnoCalendarioFacade;
import com.diario.de.classe.model.AnoCalendario;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.AnoCalendarioService;
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
public class AnoCalendarioFacadeImpl implements AnoCalendarioFacade {
    final private static Logger LOG = LogManager.getLogger(AnoCalendarioFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    AnoCalendarioService anoCalendarioService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosAnosCalendarios() {
        List<AnoCalendario> entities = anoCalendarioService.buscarTodosAnosCalendarios();
        List<AnoCalendarioDTO> dtos = entities.stream().map(AnoCalendarioDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarAnoCalendarioPoridAnoCalendario(Long idAnoCalendario) {
        AnoCalendario entity = anoCalendarioService.buscarAnoCalendarioPoridAnoCalendario(idAnoCalendario);
        AnoCalendarioDTO dto = entity != null ? new AnoCalendarioDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarAnoCalendario(AnoCalendarioDTO anoCalendarioDTO) {
        AnoCalendario entity = new AnoCalendario();
        BeanUtils.copyProperties(anoCalendarioDTO, entity);
        AnoCalendario salvo = anoCalendarioService.criarAnoCalendario(entity);
        return defaultHandleResponse.responseHandler(new AnoCalendarioDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarAnoCalendario(Long idAnoCalendario, AnoCalendarioDTO anoCalendarioDTO) {
        AnoCalendario entity = anoCalendarioService.buscarAnoCalendarioPoridAnoCalendario(idAnoCalendario);
        if(!ObjectUtils.isEmpty(entity)) {
            AnoCalendario atualizado = new AnoCalendario();
            BeanUtils.copyProperties(anoCalendarioDTO, atualizado);
            anoCalendarioService.atualizarAnoCalendario(idAnoCalendario, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new AnoCalendarioDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarAnoCalendario(Long idAnoCalendario) {
        AnoCalendario entity = anoCalendarioService.buscarAnoCalendarioPoridAnoCalendario(idAnoCalendario);
        if(!ObjectUtils.isEmpty(entity)) anoCalendarioService.deletarAnoCalendario(entity);
        return defaultHandleResponse.responseHandler(new AnoCalendarioDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }


}
