package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Curso;
import com.diario.de.classe.model.Ensino;
import com.diario.de.classe.model.Grau;
import com.diario.de.classe.model.Serie;
import com.diario.de.classe.populator.CursoPopulator;
import com.diario.de.classe.repository.jpa.CursoRepositoryJpa;
import com.diario.de.classe.service.CursoService;
import com.diario.de.classe.service.EnsinoService;
import com.diario.de.classe.service.GrauService;
import com.diario.de.classe.service.SerieService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {
    final private static Logger LOG = LogManager.getLogger(CursoServiceImpl.class);

    @Autowired
    CursoRepositoryJpa cursoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    CursoPopulator cursoPopulator;

    @Autowired
    private EnsinoService ensinoService;

    @Autowired
    private GrauService grauService;

    @Autowired
    private SerieService serieService;


    @Override
    public List<Curso> buscarTodosCursos() {
        return cursoRepositoryJpa.findAll();
    }

    @Override
    public Curso buscarCursoPoridCurso(Long idCurso) {
        Optional<Curso> curso = cursoRepositoryJpa.findById(idCurso);
        return curso.orElse(null);
    }

    @Override
    public Curso criarCurso(Curso curso) {
        if (!ObjectUtils.isEmpty(curso)) {
            return cursoRepositoryJpa.save(curso);
        }
        return curso;
    }

    @Override
    public Curso atualizarCurso(Curso cursoDoBanco) {
        if (!ObjectUtils.isEmpty(cursoDoBanco)) {
            return cursoRepositoryJpa.save(cursoDoBanco);
        }
        return cursoDoBanco;
    }

    @Override
    public Curso deletarCurso(Curso curso) {
        cursoRepositoryJpa.delete(curso);
        return curso;
    }
} 