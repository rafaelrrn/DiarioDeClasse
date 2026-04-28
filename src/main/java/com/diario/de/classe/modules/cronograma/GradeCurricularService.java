package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.GradeCurricularDTO;
import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.modules.turma.ClasseRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class GradeCurricularService {

    private final GradeCurricularRepository repository;
    private final ClasseRepository classeRepository;

    public GradeCurricularService(GradeCurricularRepository repository,
                                  ClasseRepository classeRepository) {
        this.repository = repository;
        this.classeRepository = classeRepository;
    }

    public List<GradeCurricularDTO> buscarTodos() {
        return repository.findAll().stream().map(GradeCurricularDTO::new).toList();
    }

    public GradeCurricularDTO buscarPorId(Long id) {
        return new GradeCurricularDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grade curricular não encontrada com id: " + id)));
    }

    public List<GradeCurricularDTO> buscarPorClasse(Long idClasse) {
        return repository.findByClasse_IdClasse(idClasse).stream()
                .sorted(Comparator.comparingInt(GradeCurricular::getDiaSemana)
                        .thenComparingInt(GradeCurricular::getNumeroAula))
                .map(GradeCurricularDTO::new)
                .toList();
    }

    @Transactional
    public GradeCurricularDTO criar(GradeCurricular dados, Long idClasse) {
        Classe classe = classeRepository.findById(idClasse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classe não encontrada com id: " + idClasse));

        if (repository.existsByClasse_IdClasseAndDiaSemanaAndNumeroAula(
                idClasse, dados.getDiaSemana(), dados.getNumeroAula())) {
            throw new BusinessException(
                    "Já existe um horário para o dia " + dados.getDiaSemana()
                    + ", aula " + dados.getNumeroAula() + " nesta classe.");
        }

        dados.setClasse(classe);
        return new GradeCurricularDTO(repository.save(dados));
    }

    @Transactional
    public GradeCurricularDTO atualizar(Long id, GradeCurricular dados) {
        GradeCurricular existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grade curricular não encontrada com id: " + id));

        existente.setDiaSemana(dados.getDiaSemana());
        existente.setNumeroAula(dados.getNumeroAula());
        existente.setHorarioInicio(dados.getHorarioInicio());
        existente.setHorarioFim(dados.getHorarioFim());

        return new GradeCurricularDTO(repository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        GradeCurricular existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grade curricular não encontrada com id: " + id));
        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
