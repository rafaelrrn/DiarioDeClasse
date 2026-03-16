package com.diario.de.classe.modules.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório de avaliações.
 *
 * <p>Todas as queries filtram por {@code ativo = true} para ignorar
 * registros desativados via soft delete.
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    /**
     * Lista todas as avaliações ativas (não deletadas via soft delete).
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.ativo = true")
    List<Avaliacao> findAllAtivos();

    /**
     * Lista avaliações ativas de uma disciplina específica.
     * Usado para calcular a média ponderada de um aluno por disciplina.
     *
     * @param idDisciplina ID da disciplina
     */
    @Query("SELECT a FROM Avaliacao a WHERE a.disciplina.idDisciplina = :idDisciplina AND a.ativo = true")
    List<Avaliacao> findAtivasByDisciplina(@Param("idDisciplina") Long idDisciplina);
}
