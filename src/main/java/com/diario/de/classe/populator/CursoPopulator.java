package com.diario.de.classe.populator;

import com.diario.de.classe.model.Curso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CursoPopulator {
    final private static Logger LOG = LogManager.getLogger(CursoPopulator.class);

    public Curso atualizaCurso(Curso cursoRecebido, Curso cursoDoBanco) {
        if (cursoRecebido.getEnsino() != null) cursoDoBanco.setEnsino(cursoRecebido.getEnsino());
        if (cursoRecebido.getGrau() != null) cursoDoBanco.setGrau(cursoRecebido.getGrau());
        if (cursoRecebido.getSerie() != null) cursoDoBanco.setSerie(cursoRecebido.getSerie());
        if (cursoRecebido.getCreatedAt() != null) cursoDoBanco.setCreatedAt(cursoRecebido.getCreatedAt());
        if (cursoRecebido.getUpdatedAt() != null) cursoDoBanco.setUpdatedAt(cursoRecebido.getUpdatedAt());
        return cursoDoBanco;
    }
} 