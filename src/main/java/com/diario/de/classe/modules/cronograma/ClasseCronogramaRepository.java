package com.diario.de.classe.modules.cronograma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasseCronogramaRepository extends JpaRepository<ClasseCronograma, Long> {

    List<ClasseCronograma> findByCronogramaAnual_IdCronogramaAnual(Long idCronogramaAnual);

    List<ClasseCronograma> findByClasse_IdClasse(Long idClasse);

    boolean existsByClasse_IdClasseAndCronogramaAnual_IdCronogramaAnual(Long idClasse, Long idCronogramaAnual);
}
