package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepositoryJpa extends JpaRepository<Pessoa, Long> {
} 