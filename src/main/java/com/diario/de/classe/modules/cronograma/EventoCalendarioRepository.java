package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Long> {

    List<EventoCalendario> findByCronogramaAnual_IdCronogramaAnualOrderByDataInicio(Long idCronogramaAnual);
}
