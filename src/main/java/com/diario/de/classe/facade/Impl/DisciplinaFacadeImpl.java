package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.DisciplinaFacade;
import com.diario.de.classe.model.Disciplina;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.DisciplinaService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.DisciplinaDTO;
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
public class DisciplinaFacadeImpl implements DisciplinaFacade {
    final private static Logger LOG = LogManager.getLogger(DisciplinaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private DisciplinaService disciplinaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasDisciplinas() {
        List<Disciplina> entities = disciplinaService.buscarTodasDisciplinas();
        List<DisciplinaDTO> dtos = entities.stream().map(DisciplinaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarDisciplinaPoridDisciplina(Long idDisciplina) {
        Disciplina entity = disciplinaService.buscarDisciplinaPoridDisciplina(idDisciplina);
        DisciplinaDTO dto = entity != null ? new DisciplinaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarDisciplina(DisciplinaDTO disciplinaDTO) {
        Disciplina entity = new Disciplina();
        BeanUtils.copyProperties(disciplinaDTO, entity);
        Disciplina salvo = disciplinaService.criarDisciplina(entity);
        return defaultHandleResponse.responseHandler(new DisciplinaDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarDisciplina(Long idDisciplina, DisciplinaDTO disciplinaDTO) {
        Disciplina entity = disciplinaService.buscarDisciplinaPoridDisciplina(idDisciplina);
        if(!ObjectUtils.isEmpty(entity)) {
            Disciplina atualizado = new Disciplina();
            BeanUtils.copyProperties(disciplinaDTO, atualizado);
            disciplinaService.atualizarDisciplina(idDisciplina, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new DisciplinaDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarDisciplina(Long idDisciplina) {
        Disciplina disciplina = disciplinaService.buscarDisciplinaPoridDisciplina(idDisciplina);
        if(!ObjectUtils.isEmpty(disciplina)) disciplinaService.deletarDisciplina(disciplina);
        return defaultHandleResponse.responseHandler(disciplina, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
