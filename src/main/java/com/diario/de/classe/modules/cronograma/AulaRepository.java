package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByClasse_IdClasseAndDataAula(Long idClasse, LocalDate dataAula);

    List<Aula> findByPeriodoLetivo_IdPeriodoLetivoAndClasse_IdClasse(Long idPeriodoLetivo, Long idClasse);

    boolean existsByClasse_IdClasseAndDataAulaAndNumeroAula(Long idClasse, LocalDate dataAula, Integer numeroAula);
}
