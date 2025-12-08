package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.ComponenteCurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteCurricularRepositoryJpa extends JpaRepository<ComponenteCurricular, Long> {
} 