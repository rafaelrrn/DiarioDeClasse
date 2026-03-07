package com.diario.de.classe.modules.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoAvaliacaoRepository extends JpaRepository<AlunoAvaliacao, Long> {
}
