package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.AnoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnoCalendarioRepositoryJpa extends JpaRepository<AnoCalendario, Long> {
} 