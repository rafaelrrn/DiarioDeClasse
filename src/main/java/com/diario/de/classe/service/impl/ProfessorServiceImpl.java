package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.old.Professor;
import com.diario.de.classe.populator.GlobalPopulator;
import com.diario.de.classe.repository.jpa.ProfessorRepositoryJpa;
import com.diario.de.classe.service.old.ProfessorService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    final private static Logger LOG = LogManager.getLogger(Professor.class);

    @Autowired
    private ProfessorRepositoryJpa professorRepositoryJpa;

    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;

    @Autowired
    private GlobalPopulator globalPopulator;

    @Override
    public List<Professor> buscarTodosProfessores() {
        return professorRepositoryJpa.findAll();
    }

    @Override
    public Professor buscarProfessorByCodPrf(Long codPrf){


        Optional<Professor> professor = professorRepositoryJpa.findById(codPrf);
        return professor.orElseGet(Professor::new);

    }

    @Override
    public Professor criarProfessor(Professor professor) {
        return professorRepositoryJpa.save(professor);
    }

    @Override
    public Professor atualizarProfessor(Long codPrf, String professorBody, Professor professorDoBanco) {

        Professor professorRecebido = conversorObjetoEntidadeUtil.converterObjetoEmEntidade(professorBody, Professor.class);

        // Usar o GenericPopulator para copiar as propriedades não nulas
        globalPopulator.updateGenericObj(professorRecebido, professorDoBanco);

        // Persistir as alterações no banco de dados
        return professorRepositoryJpa.save(professorDoBanco);

    }

    @Override
    public Professor deletarProfessor(Professor professor) {
         professorRepositoryJpa.delete(professor);
        return professor;
    }


}
