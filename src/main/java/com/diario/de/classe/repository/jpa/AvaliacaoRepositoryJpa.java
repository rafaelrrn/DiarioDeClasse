package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepositoryJpa extends JpaRepository<Avaliacao, Long> {
} 