package com.diario.de.classe.modules.frequencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoFrequenciaRepository extends JpaRepository<AlunoFrequencia, Long> {
}
