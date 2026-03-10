package com.diario.de.classe.modules.turma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, Long> {
}
