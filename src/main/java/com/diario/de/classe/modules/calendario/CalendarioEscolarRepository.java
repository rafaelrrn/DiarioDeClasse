package com.diario.de.classe.modules.calendario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioEscolarRepository extends JpaRepository<CalendarioEscolar, Long> {
}
