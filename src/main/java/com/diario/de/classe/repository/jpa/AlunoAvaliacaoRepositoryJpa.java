package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.AlunoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoAvaliacaoRepositoryJpa extends JpaRepository<AlunoAvaliacao, Long> {
} 