package com.diario.de.classe.populator;

import com.diario.de.classe.model.Disciplina;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class DisciplinaPopulator {
    final private static Logger LOG = LogManager.getLogger(DisciplinaPopulator.class);

    public Disciplina atualizaDisciplina(Disciplina disciplinaRecebida, Disciplina disciplinaDoBanco) {
        if (disciplinaRecebida.getNome() != null) disciplinaDoBanco.setNome(disciplinaRecebida.getNome());
        if (disciplinaRecebida.getCreatedAt() != null) disciplinaDoBanco.setCreatedAt(disciplinaRecebida.getCreatedAt());
        if (disciplinaRecebida.getUpdatedAt() != null) disciplinaDoBanco.setUpdatedAt(disciplinaRecebida.getUpdatedAt());
        return disciplinaDoBanco;
    }
} 