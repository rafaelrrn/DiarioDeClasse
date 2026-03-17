package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do AlunoTurmaService.
 *
 * Cobrem as regras de matrícula definidas em INSTRUCTIONS.md §6 (ETAPA 6):
 * - Um aluno não pode ter duas matrículas ativas na mesma turma
 * - Matrículas desativadas (soft delete) não bloqueiam nova matrícula
 * - Desativação é sempre soft delete (ativo = false, deletedAt = now)
 */
@ExtendWith(MockitoExtension.class)
class AlunoTurmaServiceTest {

    @Mock private AlunoTurmaRepository repository;
    @Mock private PessoaRepository pessoaRepository;
    @Mock private TurmaRepository turmaRepository;

    @InjectMocks
    private AlunoTurmaService service;

    // -------------------------------------------------------------------------
    // matricular
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("matricular — regra de negócio de matrícula única por turma")
    class MatricularTest {

        @Test
        @DisplayName("Deve criar matrícula quando aluno, turma existem e não há duplicidade ativa")
        void deveCriarMatricula_quandoDadosValidos() {
            Pessoa aluno = new Pessoa();
            aluno.setIdPessoa(1L);
            aluno.setNome("João Silva");

            Turma turma = new Turma();
            turma.setIdTurma(1L);
            turma.setNome("5º Ano A");

            AlunoTurma matriculaSalva = new AlunoTurma();
            matriculaSalva.setPessoaAluno(aluno);
            matriculaSalva.setTurma(turma);

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
            when(repository.existsByPessoaAlunoIdPessoaAndTurmaIdTurmaAndAtivoTrue(1L, 1L))
                    .thenReturn(false);
            when(repository.save(any(AlunoTurma.class))).thenReturn(matriculaSalva);

            AlunoTurma resultado = service.matricular(1L, 1L, "Observação de teste");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getPessoaAluno().getNome()).isEqualTo("João Silva");
            verify(repository).save(any(AlunoTurma.class));
        }

        @Test
        @DisplayName("Deve persistir a observação informada na matrícula")
        void devePersistirObservacao_quandoInformada() {
            Pessoa aluno = new Pessoa();
            Turma turma = new Turma();

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
            when(repository.existsByPessoaAlunoIdPessoaAndTurmaIdTurmaAndAtivoTrue(1L, 1L))
                    .thenReturn(false);
            when(repository.save(any(AlunoTurma.class))).thenAnswer(i -> i.getArgument(0));

            // Captura o objeto salvo para verificar a observação
            ArgumentCaptor<AlunoTurma> captor = ArgumentCaptor.forClass(AlunoTurma.class);

            service.matricular(1L, 1L, "Transferido de outra escola");

            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getObs()).isEqualTo("Transferido de outra escola");
        }

        @Test
        @DisplayName("Deve lançar BusinessException quando aluno já tem matrícula ativa nesta turma")
        void deveLancarBusinessException_quandoMatriculaDuplicada() {
            Pessoa aluno = new Pessoa();
            Turma turma = new Turma();

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
            // Matrícula ativa já existe
            when(repository.existsByPessoaAlunoIdPessoaAndTurmaIdTurmaAndAtivoTrue(1L, 1L))
                    .thenReturn(true);

            assertThatThrownBy(() -> service.matricular(1L, 1L, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("já está matriculado");

            // Nenhuma persistência deve ocorrer após a detecção da duplicidade
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando aluno não existe")
        void deveLancarException_quandoAlunoNaoExiste() {
            when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.matricular(99L, 1L, null))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Aluno não encontrado");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando turma não existe")
        void deveLancarException_quandoTurmaNaoExiste() {
            Pessoa aluno = new Pessoa();
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(turmaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.matricular(1L, 99L, null))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Turma não encontrada");
        }
    }

    // -------------------------------------------------------------------------
    // desativar (soft delete)
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("desativar — soft delete de matrícula")
    class DesativarTest {

        @Test
        @DisplayName("Deve desativar matrícula setando ativo=false e deletedAt com a data atual")
        void deveDesativarMatricula_preservandoHistorico() {
            AlunoTurma matricula = new AlunoTurma();
            matricula.setAtivo(true);

            when(repository.findById(1L)).thenReturn(Optional.of(matricula));
            when(repository.save(any(AlunoTurma.class))).thenAnswer(i -> i.getArgument(0));

            service.desativar(1L);

            // Captura o objeto salvo para verificar o soft delete
            ArgumentCaptor<AlunoTurma> captor = ArgumentCaptor.forClass(AlunoTurma.class);
            verify(repository).save(captor.capture());

            assertThat(captor.getValue().getAtivo()).isFalse();
            // deletedAt deve ter sido definido (não nulo)
            assertThat(captor.getValue().getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException ao tentar desativar matrícula inexistente")
        void deveLancarException_quandoMatriculaNaoExiste() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.desativar(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Matrícula não encontrada");
        }
    }
}
