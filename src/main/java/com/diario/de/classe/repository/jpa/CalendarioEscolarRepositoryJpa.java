package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.CalendarioEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioEscolarRepositoryJpa extends JpaRepository<CalendarioEscolar, Long> {
} 