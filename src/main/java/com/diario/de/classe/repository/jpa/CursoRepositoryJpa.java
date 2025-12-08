package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepositoryJpa extends JpaRepository<Curso, Long> {
} 