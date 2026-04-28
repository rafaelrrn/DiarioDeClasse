package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CronogramaAnualRepository extends JpaRepository<CronogramaAnual, Long> {

    List<CronogramaAnual> findAllByAtivoTrue();

    Optional<CronogramaAnual> findByIdCronogramaAnualAndAtivoTrue(Long id);

    boolean existsByAno(Integer ano);
}
