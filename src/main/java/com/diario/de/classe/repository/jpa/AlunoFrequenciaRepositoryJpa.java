package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.AlunoFrequencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoFrequenciaRepositoryJpa extends JpaRepository<AlunoFrequencia, Long> {
} 