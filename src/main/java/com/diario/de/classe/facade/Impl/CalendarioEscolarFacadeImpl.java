package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.CalendarioEscolarDTO;
import com.diario.de.classe.facade.CalendarioEscolarFacade;
import com.diario.de.classe.mapper.CalendarioEscolarMapper;
import com.diario.de.classe.model.CalendarioEscolar;
import com.diario.de.classe.service.CalendarioEscolarService;
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
public class CalendarioEscolarFacadeImpl implements CalendarioEscolarFacade {
    final private static Logger LOG = LogManager.getLogger(CalendarioEscolarFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private CalendarioEscolarMapper calendarioEscolarMapper;
    @Autowired
    private CalendarioEscolarService calendarioEscolarService;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosCalendariosEscolares() {
        List<CalendarioEscolar> entities = calendarioEscolarService.buscarTodosCalendariosEscolares();
        List<CalendarioEscolarDTO> dtos = entities.stream().map(CalendarioEscolarDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarCalendarioEscolarPoridCalendarioEscolar(Long idCalendarioEscolar) {
        CalendarioEscolar entity = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(idCalendarioEscolar);
        CalendarioEscolarDTO dto = entity != null ? new CalendarioEscolarDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarCalendarioEscolar(CalendarioEscolarDTO calendarioEscolarDTO) {
        CalendarioEscolar entity = calendarioEscolarMapper.toEntity(calendarioEscolarDTO);
        CalendarioEscolar salvo = calendarioEscolarService.criarCalendarioEscolar(entity);
        return defaultHandleResponse.responseHandler(calendarioEscolarMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarCalendarioEscolar(Long idCalendarioEscolar, CalendarioEscolarDTO calendarioEscolarDTO) {
        CalendarioEscolar calendarioEscolarDoBanco = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(idCalendarioEscolar);
        if(!ObjectUtils.isEmpty(calendarioEscolarDoBanco)) {
            calendarioEscolarMapper.updateEntityFromDTO(calendarioEscolarDTO, calendarioEscolarDoBanco);
            calendarioEscolarService.atualizarCalendarioEscolar(calendarioEscolarDoBanco);
        }
        return defaultHandleResponse.responseHandler(calendarioEscolarMapper.toDTO(calendarioEscolarDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarCalendarioEscolar(Long idCalendarioEscolar) {
        CalendarioEscolar calendarioEscolar = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(idCalendarioEscolar);
        if(!ObjectUtils.isEmpty(calendarioEscolar)) calendarioEscolarService.deletarCalendarioEscolar(calendarioEscolar);
        return defaultHandleResponse.responseHandler(calendarioEscolar, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
