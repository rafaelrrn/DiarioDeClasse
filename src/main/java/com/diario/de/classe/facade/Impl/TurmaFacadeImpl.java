package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.TurmaDTO;
import com.diario.de.classe.facade.TurmaFacade;
import com.diario.de.classe.model.TipoPessoa;
import com.diario.de.classe.model.Turma;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.TurmaService;
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
public class TurmaFacadeImpl implements TurmaFacade {
    final private static Logger LOG = LogManager.getLogger(TurmaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    TurmaService turmaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasTurmas() {
        List<Turma> entities = turmaService.buscarTodasTurmas();
        List<TurmaDTO> dtos = entities.stream().map(TurmaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarTurmaPoridTurma(Long idTurma) {
        Turma entity = turmaService.buscarTurmaPoridTurma(idTurma);
        TurmaDTO dto = entity != null ? new TurmaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarTurma(TurmaDTO turmaDTO) {
        Turma entity = new Turma();
        BeanUtils.copyProperties(turmaDTO, entity);
        Turma salvo = turmaService.criarTurma(entity);
        return defaultHandleResponse.responseHandler(new TurmaDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarTurma(Long idTurma, TurmaDTO turmaDTO) {
        Turma entity = turmaService.buscarTurmaPoridTurma(idTurma);
        if(!ObjectUtils.isEmpty(entity)) {
            Turma atualizado = new Turma();
            BeanUtils.copyProperties(turmaDTO, atualizado);
            turmaService.atualizarTurma(idTurma, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new TurmaDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarTurma(Long idTurma) {
        Turma turma = turmaService.buscarTurmaPoridTurma(idTurma);
        if(!ObjectUtils.isEmpty(turma)) turmaService.deletarTurma(turma);
        return defaultHandleResponse.responseHandler(turma, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
