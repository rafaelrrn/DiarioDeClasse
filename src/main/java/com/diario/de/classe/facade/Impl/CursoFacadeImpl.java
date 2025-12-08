package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ContatoPessoaFacade;
import com.diario.de.classe.facade.CursoFacade;
import com.diario.de.classe.mapper.CursoMapper;
import com.diario.de.classe.model.*;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.CursoService;
import com.diario.de.classe.service.EnsinoService;
import com.diario.de.classe.service.GrauService;
import com.diario.de.classe.service.SerieService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.CursoDTO;
import org.springframework.beans.BeanUtils;
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
public class CursoFacadeImpl implements CursoFacade {
    final private static Logger LOG = LogManager.getLogger(CursoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private CursoMapper cursoMapper;
    @Autowired
    private CursoService cursoService;

    @Autowired
    private EnsinoService ensinoService;

    @Autowired
    private GrauService grauService;

    @Autowired
    private SerieService serieService;

    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosCursos() {
        List<Curso> entities = cursoService.buscarTodosCursos();
        List<CursoDTO> dtos = entities.stream().map(CursoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarCursoPoridCurso(Long idCurso) {
        Curso entity = cursoService.buscarCursoPoridCurso(idCurso);
        CursoDTO dto = entity != null ? new CursoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarCurso(CursoDTO cursoDTO) {
        Curso entity = cursoMapper.toEntity(cursoDTO);
        Curso salvo = cursoService.criarCurso(entity);
        return defaultHandleResponse.responseHandler(cursoMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarCurso(Long idCurso, CursoDTO cursoDTO) {
        Curso cursoDoBanco = cursoService.buscarCursoPoridCurso(idCurso);
        if(!ObjectUtils.isEmpty(cursoDoBanco)) {
            cursoMapper.updateEntityFromDTO(cursoDTO,cursoDoBanco);
            cursoService.atualizarCurso(cursoDoBanco);
        }
        return defaultHandleResponse.responseHandler(cursoMapper.toDTO(cursoDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarCurso(Long idCurso) {
        Curso curso = cursoService.buscarCursoPoridCurso(idCurso);
        if(!ObjectUtils.isEmpty(curso)) cursoService.deletarCurso(curso);
        return defaultHandleResponse.responseHandler(curso, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
