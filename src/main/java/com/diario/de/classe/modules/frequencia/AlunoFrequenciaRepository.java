package com.diario.de.classe.modules.frequencia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoFrequenciaRepository extends JpaRepository<AlunoFrequencia, Long> {

    List<AlunoFrequencia> findAllByAtivoTrue();

    List<AlunoFrequencia> findByAluno_IdPessoaAndAtivoTrue(Long idAluno);

    List<AlunoFrequencia> findByAula_IdAula(Long idAula);

    Optional<AlunoFrequencia> findByAula_IdAulaAndAluno_IdPessoa(Long idAula, Long idAluno);

    List<AlunoFrequencia> findByAluno_IdPessoaAndAula_PeriodoLetivo_IdPeriodoLetivo(Long idAluno, Long idPeriodoLetivo);

    long countByAluno_IdPessoaAndAtivoTrue(Long idAluno);

    long countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(Long idAluno, String tipoFrequencia);
}
