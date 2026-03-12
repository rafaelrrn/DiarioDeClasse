package com.diario.de.classe.modules.turma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, Long> {

    /**
     * Lista todas as matrículas ativas, excluindo registros com soft delete.
     * Usar sempre este método nas listagens gerais — nunca findAll() diretamente.
     */
    @Query("SELECT at FROM AlunoTurma at WHERE at.ativo = true")
    List<AlunoTurma> findAllAtivos();

    /**
     * Lista as matrículas ativas de uma turma específica.
     * Usado no endpoint GET /v1/turmas/{id}/alunos.
     */
    @Query("SELECT at FROM AlunoTurma at WHERE at.turma.idTurma = :idTurma AND at.ativo = true")
    List<AlunoTurma> findAtivosByTurmaId(@Param("idTurma") Long idTurma);

    /**
     * Verifica se um aluno já está matriculado em uma turma (matrícula ativa).
     * Usado para evitar matrículas duplicadas antes de persistir.
     */
    boolean existsByPessoaAlunoIdPessoaAndTurmaIdTurmaAndAtivoTrue(Long idPessoa, Long idTurma);
}
