package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.old.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepositoryJpa extends JpaRepository<Professor, Long> {
}
