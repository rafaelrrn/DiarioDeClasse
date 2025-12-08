package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepositoryJpa extends JpaRepository<Contato, Long> {
} 