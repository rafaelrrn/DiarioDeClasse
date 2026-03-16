package com.diario.de.classe.modules.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de notas de alunos.
 *
 * <p>Todas as queries filtram por {@code ativo = true} para ignorar
 * registros desativados via soft delete.
 */
@Repository
public interface AlunoAvaliacaoRepository extends JpaRepository<AlunoAvaliacao, Long> {

    /**
     * Lista todas as notas ativas (não deletadas via soft delete).
     */
    @Query("SELECT aa FROM AlunoAvaliacao aa WHERE aa.ativo = true")
    List<AlunoAvaliacao> findAllAtivos();

    /**
     * Lista todas as notas ativas de um aluno específico.
     * Usado para gerar o boletim e consultar notas por aluno.
     *
     * @param idAluno ID da Pessoa com tipo ALUNO
     */
    @Query("SELECT aa FROM AlunoAvaliacao aa WHERE aa.pessoaAluno.idPessoa = :idAluno AND aa.ativo = true")
    List<AlunoAvaliacao> findAtivasByAluno(@Param("idAluno") Long idAluno);

    /**
     * Lista notas ativas de um aluno em uma disciplina específica.
     * Usado no cálculo de média ponderada por disciplina.
     *
     * @param idAluno      ID do aluno
     * @param idDisciplina ID da disciplina
     */
    @Query("""
            SELECT aa FROM AlunoAvaliacao aa
            WHERE aa.pessoaAluno.idPessoa = :idAluno
              AND aa.avaliacao.disciplina.idDisciplina = :idDisciplina
              AND aa.ativo = true
            """)
    List<AlunoAvaliacao> findAtivasByAlunoAndDisciplina(
            @Param("idAluno") Long idAluno,
            @Param("idDisciplina") Long idDisciplina);

    /**
     * Lista notas ativas lançadas em uma avaliação específica.
     * Usado para consultar quem já tem nota em um lançamento em lote.
     *
     * @param idAvaliacao ID da avaliação
     */
    @Query("SELECT aa FROM AlunoAvaliacao aa WHERE aa.avaliacao.idAvaliacao = :idAvaliacao AND aa.ativo = true")
    List<AlunoAvaliacao> findAtivasByAvaliacao(@Param("idAvaliacao") Long idAvaliacao);

    /**
     * Verifica se já existe nota ativa para este aluno nesta avaliação.
     * Usado para impedir lançamento duplicado.
     *
     * @param idAluno     ID do aluno
     * @param idAvaliacao ID da avaliação
     */
    boolean existsByPessoaAlunoIdPessoaAndAvaliacaoIdAvaliacaoAndAtivoTrue(Long idAluno, Long idAvaliacao);
}
