package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodoLetivoRepository extends JpaRepository<PeriodoLetivo, Long> {

    List<PeriodoLetivo> findByCronogramaAnual_IdCronogramaAnualOrderByOrdem(Long idCronogramaAnual);

    boolean existsByCronogramaAnual_IdCronogramaAnualAndOrdem(Long idCronogramaAnual, Integer ordem);
}
