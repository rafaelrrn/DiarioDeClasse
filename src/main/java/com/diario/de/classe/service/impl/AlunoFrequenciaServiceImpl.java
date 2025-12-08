package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.AlunoFrequencia;
import com.diario.de.classe.repository.jpa.AlunoFrequenciaRepositoryJpa;
import com.diario.de.classe.service.AlunoFrequenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoFrequenciaServiceImpl implements AlunoFrequenciaService {
    @Autowired
    private AlunoFrequenciaRepositoryJpa alunoFrequenciaRepositoryJpa;

    @Override
    public List<AlunoFrequencia> buscarTodosAlunosFrequencias() {
        return alunoFrequenciaRepositoryJpa.findAll();
    }

    @Override
    public AlunoFrequencia buscarAlunoFrequenciaPorIdAlunoFrequencia(Long idAlunoFrequencia) {
        Optional<AlunoFrequencia> alunoFrequencia = alunoFrequenciaRepositoryJpa.findById(idAlunoFrequencia);
        return alunoFrequencia.orElse(null);
    }

    @Override
    public AlunoFrequencia criarAlunoFrequencia(AlunoFrequencia alunoFrequencia) {
        if (!ObjectUtils.isEmpty(alunoFrequencia)) {
            return alunoFrequenciaRepositoryJpa.save(alunoFrequencia);
        }
        return alunoFrequencia;
    }

    @Override
    public AlunoFrequencia atualizarAlunoFrequencia(AlunoFrequencia alunoFrequenciaDoBanco) {
        if (!ObjectUtils.isEmpty(alunoFrequenciaDoBanco)) {
            return alunoFrequenciaRepositoryJpa.save(alunoFrequenciaDoBanco);
        }
        return alunoFrequenciaDoBanco;
    }

    @Override
    public AlunoFrequencia deletarAlunoFrequencia(AlunoFrequencia alunoFrequencia) {
        alunoFrequenciaRepositoryJpa.delete(alunoFrequencia);
        return alunoFrequencia;
    }
} 