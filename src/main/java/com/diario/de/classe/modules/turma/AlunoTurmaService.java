package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlunoTurmaService {

    private final AlunoTurmaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final TurmaRepository turmaRepository;

    public AlunoTurmaService(AlunoTurmaRepository repository, PessoaRepository pessoaRepository,
                             TurmaRepository turmaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.turmaRepository = turmaRepository;
    }

    /**
     * Lista todas as matrículas ativas do sistema.
     * Registros com soft delete (ativo=false) são excluídos automaticamente.
     */
    public List<AlunoTurma> buscarTodos() {
        return repository.findAllAtivos();
    }

    /**
     * Busca uma matrícula pelo ID.
     *
     * @throws ResourceNotFoundException se a matrícula não existir
     */
    public AlunoTurma buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada com id: " + id));
    }

    /**
     * Lista todos os alunos ativamente matriculados em uma turma.
     *
     * @param idTurma ID da turma
     * @return lista de matrículas ativas da turma
     * @throws ResourceNotFoundException se a turma não existir
     */
    public List<AlunoTurma> buscarAlunosPorTurma(Long idTurma) {
        if (!turmaRepository.existsById(idTurma)) {
            throw new ResourceNotFoundException("Turma não encontrada com id: " + idTurma);
        }
        return repository.findAtivosByTurmaId(idTurma);
    }

    /**
     * Matricula um aluno em uma turma, aplicando as regras de negócio.
     *
     * Regras:
     * - O aluno (Pessoa) deve existir no banco.
     * - A turma deve existir no banco.
     * - O aluno não pode estar matriculado na mesma turma mais de uma vez simultaneamente.
     *   Matrículas desativadas (soft delete) não bloqueiam uma nova matrícula.
     *
     * @param idAluno ID da Pessoa do tipo ALUNO
     * @param idTurma ID da Turma
     * @param obs     Observação opcional sobre a matrícula
     * @return matrícula persistida
     * @throws ResourceNotFoundException se aluno ou turma não forem encontrados
     * @throws BusinessException         se o aluno já estiver matriculado ativamente nesta turma
     */
    public AlunoTurma matricular(Long idAluno, Long idTurma, String obs) {
        Pessoa aluno = pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + idTurma));

        // Impede matrícula duplicada — verifica apenas matrículas ativas (ativo=true)
        if (repository.existsByPessoaAlunoIdPessoaAndTurmaIdTurmaAndAtivoTrue(idAluno, idTurma)) {
            throw new BusinessException(
                    "Aluno já está matriculado nesta turma. Desative a matrícula atual antes de criar uma nova.");
        }

        AlunoTurma matricula = new AlunoTurma();
        matricula.setPessoaAluno(aluno);
        matricula.setTurma(turma);
        matricula.setObs(obs);

        return repository.save(matricula);
    }

    /**
     * Desativa uma matrícula (soft delete).
     *
     * O histórico de matrícula é preservado no banco para fins pedagógicos e legais.
     * Nunca usar repository.delete() diretamente — sempre chamar este método.
     *
     * @param id ID da matrícula a desativar
     * @throws ResourceNotFoundException se a matrícula não existir
     */
    public void desativar(Long id) {
        AlunoTurma matricula = buscarPorId(id);
        matricula.setAtivo(false);
        matricula.setDeletedAt(LocalDateTime.now());
        repository.save(matricula);
    }
}
