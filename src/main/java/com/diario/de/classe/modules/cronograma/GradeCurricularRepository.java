package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeCurricularRepository extends JpaRepository<GradeCurricular, Long> {

    List<GradeCurricular> findByClasse_IdClasse(Long idClasse);

    boolean existsByClasse_IdClasseAndDiaSemanaAndNumeroAula(Long idClasse, Integer diaSemana, Integer numeroAula);
}
