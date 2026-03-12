package com.diario.de.classe.modules.frequencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de frequência de alunos.
 *
 * <p>Todas as queries filtram por {@code ativo = true} para ignorar
 * registros desativados via soft delete.
 */
@Repository
public interface AlunoFrequenciaRepository extends JpaRepository<AlunoFrequencia, Long> {

    /**
     * Lista todos os registros de frequência ativos (não deletados).
     */
    @Query("SELECT f FROM AlunoFrequencia f WHERE f.ativo = true")
    List<AlunoFrequencia> findAllAtivos();

    /**
     * Lista todas as frequências ativas de um aluno específico.
     *
     * @param idAluno ID da Pessoa com tipo ALUNO
     */
    @Query("SELECT f FROM AlunoFrequencia f WHERE f.pessoaAluno.idPessoa = :idAluno AND f.ativo = true")
    List<AlunoFrequencia> findAtivosByAluno(@Param("idAluno") Long idAluno);

    /**
     * Lista frequências ativas de um aluno em um calendário escolar específico.
     *
     * @param idAluno             ID do aluno
     * @param idCalendarioEscolar ID do calendário (aula/período)
     */
    @Query("""
            SELECT f FROM AlunoFrequencia f
            WHERE f.pessoaAluno.idPessoa = :idAluno
              AND f.calendarioEscolar.idCalendarioEscolar = :idCalendario
              AND f.ativo = true
            """)
    List<AlunoFrequencia> findAtivosByAlunoAndCalendario(
            @Param("idAluno") Long idAluno,
            @Param("idCalendario") Long idCalendarioEscolar);

    /**
     * Conta o total de registros ativos de um aluno (= total de aulas registradas).
     *
     * <p>Usado como denominador na fórmula: {@code percentual = (presencas / totalAulas) * 100}.
     *
     * @param idAluno ID do aluno
     */
    @Query("SELECT COUNT(f) FROM AlunoFrequencia f WHERE f.pessoaAluno.idPessoa = :idAluno AND f.ativo = true")
    long countTotalAulasByAluno(@Param("idAluno") Long idAluno);

    /**
     * Conta os registros de um tipo específico (PRESENTE, FALTA ou FALTA_JUSTIFICADA) para um aluno.
     *
     * @param idAluno ID do aluno
     * @param tipo    tipo de frequência a contar
     */
    @Query("""
            SELECT COUNT(f) FROM AlunoFrequencia f
            WHERE f.pessoaAluno.idPessoa = :idAluno
              AND f.tipoFrequencia = :tipo
              AND f.ativo = true
            """)
    long countByAlunoAndTipo(@Param("idAluno") Long idAluno, @Param("tipo") TipoFrequencia tipo);

    /**
     * Verifica se já existe um registro de frequência ativo para este aluno neste calendário.
     * Usado para evitar lançamentos duplicados.
     *
     * @param idAluno             ID do aluno
     * @param idCalendarioEscolar ID do calendário escolar
     */
    boolean existsByPessoaAlunoIdPessoaAndCalendarioEscolarIdCalendarioEscolarAndAtivoTrue(
            Long idAluno, Long idCalendarioEscolar);
}
