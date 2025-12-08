package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.TurnoDTO;
import com.diario.de.classe.facade.TurnoFacade;
import com.diario.de.classe.model.Turno;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.TurnoService;
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
public class TurnoFacadeImpl implements TurnoFacade {
    final private static Logger LOG = LogManager.getLogger(TurnoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    TurnoService turnoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosTurnos() {
        List<Turno> entities = turnoService.buscarTodosTurnos();
        List<TurnoDTO> dtos = entities.stream().map(TurnoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarTurnoPoridTurno(Long idTurno) {
        Turno entity = turnoService.buscarTurnoPoridTurno(idTurno);
        TurnoDTO dto = entity != null ? new TurnoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarTurno(TurnoDTO turnoDTO) {
        Turno entity = new Turno();
        BeanUtils.copyProperties(turnoDTO, entity);
        Turno salvo = turnoService.criarTurno(entity);
        return defaultHandleResponse.responseHandler(new TurnoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarTurno(Long idTurno, TurnoDTO turnoDTO) {
        Turno entity = turnoService.buscarTurnoPoridTurno(idTurno);
        if(!ObjectUtils.isEmpty(entity)) {
            Turno atualizado = new Turno();
            BeanUtils.copyProperties(turnoDTO, atualizado);
            turnoService.atualizarTurno(idTurno, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new TurnoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarTurno(Long idTurno) {
        Turno turno = turnoService.buscarTurnoPoridTurno(idTurno);
        if(!ObjectUtils.isEmpty(turno)) turnoService.deletarTurno(turno);
        return defaultHandleResponse.responseHandler(turno, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
