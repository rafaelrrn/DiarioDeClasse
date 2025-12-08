package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.AlunoTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoTurmaRepositoryJpa extends JpaRepository<AlunoTurma, Long> {
} 