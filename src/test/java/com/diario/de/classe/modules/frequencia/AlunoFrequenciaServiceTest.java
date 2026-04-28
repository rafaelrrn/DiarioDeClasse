package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.cronograma.Aula;
import com.diario.de.classe.modules.cronograma.AulaRepository;
import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.modules.turma.AlunoTurmaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do AlunoFrequenciaService.
 *
 * Cobrem as regras definidas em INSTRUCTIONS.md §7.1 (LDB Art. 24):
 * - Percentual: totalPresencas / totalAulas * 100
 * - Frequência mínima legal: 75%
 * - FALTA_JUSTIFICADA conta no denominador mas não como falta para reprovação
 * - Impede lançamento duplicado para o mesmo aluno/aula
 */
@ExtendWith(MockitoExtension.class)
class AlunoFrequenciaServiceTest {

    @Mock private AlunoFrequenciaRepository repository;
    @Mock private PessoaRepository pessoaRepository;
    @Mock private AulaRepository aulaRepository;
    @Mock private AlunoTurmaRepository alunoTurmaRepository;

    @InjectMocks
    private AlunoFrequenciaService service;

    // -------------------------------------------------------------------------
    // calcularFrequencia
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("calcularFrequencia — cálculo LDB (mínimo 75%)")
    class CalcularFrequenciaTest {

        @Test
        @DisplayName("Deve calcular percentual correto: 8 presenças em 10 aulas = 80%")
        void deveCalcularPercentual_quandoHaRegistros() {
            when(pessoaRepository.existsById(1L)).thenReturn(true);
            when(repository.countByAluno_IdPessoaAndAtivoTrue(1L)).thenReturn(10L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "PRESENTE")).thenReturn(8L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA")).thenReturn(2L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA_JUSTIFICADA")).thenReturn(0L);

            FrequenciaResumoDTO resultado = service.calcularFrequencia(1L);

            assertThat(resultado.percentualPresenca()).isEqualTo(80.0);
            assertThat(resultado.totalAulas()).isEqualTo(10L);
            assertThat(resultado.totalPresencas()).isEqualTo(8L);
            assertThat(resultado.totalFaltas()).isEqualTo(2L);
            assertThat(resultado.emRiscoReprovacao()).isFalse();
        }

        @Test
        @DisplayName("Deve retornar 0% e não em risco quando nenhuma aula foi registrada")
        void deveRetornarZero_quandoSemAulas() {
            when(pessoaRepository.existsById(1L)).thenReturn(true);
            when(repository.countByAluno_IdPessoaAndAtivoTrue(1L)).thenReturn(0L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(any(), any())).thenReturn(0L);

            FrequenciaResumoDTO resultado = service.calcularFrequencia(1L);

            assertThat(resultado.percentualPresenca()).isEqualTo(0.0);
            assertThat(resultado.totalAulas()).isEqualTo(0L);
            assertThat(resultado.emRiscoReprovacao()).isFalse();
        }

        @Test
        @DisplayName("Deve marcar emRisco=true quando percentual abaixo de 75% (LDB)")
        void deveMarcaEmRisco_quandoPercentualAbaixo75() {
            when(pessoaRepository.existsById(1L)).thenReturn(true);
            when(repository.countByAluno_IdPessoaAndAtivoTrue(1L)).thenReturn(10L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "PRESENTE")).thenReturn(7L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA")).thenReturn(3L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA_JUSTIFICADA")).thenReturn(0L);

            FrequenciaResumoDTO resultado = service.calcularFrequencia(1L);

            assertThat(resultado.percentualPresenca()).isEqualTo(70.0);
            assertThat(resultado.emRiscoReprovacao()).isTrue();
        }

        @Test
        @DisplayName("Deve computar FALTA_JUSTIFICADA no total de aulas, sem contar como falta")
        void deveComputarFaltaJustificadaNoTotal_masNaoComoFalta() {
            when(pessoaRepository.existsById(1L)).thenReturn(true);
            when(repository.countByAluno_IdPessoaAndAtivoTrue(1L)).thenReturn(10L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "PRESENTE")).thenReturn(8L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA")).thenReturn(1L);
            when(repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(1L, "FALTA_JUSTIFICADA")).thenReturn(1L);

            FrequenciaResumoDTO resultado = service.calcularFrequencia(1L);

            assertThat(resultado.percentualPresenca()).isEqualTo(80.0);
            assertThat(resultado.totalFaltasJust()).isEqualTo(1L);
            assertThat(resultado.emRiscoReprovacao()).isFalse();
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando aluno não existe")
        void deveLancarException_quandoAlunoNaoExiste() {
            when(pessoaRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> service.calcularFrequencia(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Aluno não encontrado");
        }
    }

    // -------------------------------------------------------------------------
    // registrar
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("registrar — lançamento individual de frequência")
    class RegistrarTest {

        @Test
        @DisplayName("Deve registrar frequência com sucesso para aluno e aula válidos")
        void deveRegistrar_quandoDadosValidos() {
            Pessoa aluno = new Pessoa();
            aluno.setIdPessoa(1L);
            Aula aula = new Aula();
            aula.setChamadaEncerrada(false);

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
            when(repository.findByAula_IdAulaAndAluno_IdPessoa(1L, 1L)).thenReturn(Optional.empty());
            when(repository.save(any(AlunoFrequencia.class))).thenAnswer(i -> i.getArgument(0));

            AlunoFrequencia resultado = service.registrar(1L, 1L, "PRESENTE");

            assertThat(resultado.getTipoFrequencia()).isEqualTo("PRESENTE");
            verify(repository).save(any(AlunoFrequencia.class));
        }

        @Test
        @DisplayName("Deve assumir PRESENTE quando tipo não é informado (padrão)")
        void deveAssumirPresente_quandoTipoNulo() {
            Pessoa aluno = new Pessoa();
            Aula aula = new Aula();
            aula.setChamadaEncerrada(false);

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
            when(repository.findByAula_IdAulaAndAluno_IdPessoa(1L, 1L)).thenReturn(Optional.empty());
            when(repository.save(any(AlunoFrequencia.class))).thenAnswer(i -> i.getArgument(0));

            AlunoFrequencia resultado = service.registrar(1L, 1L, null);

            assertThat(resultado.getTipoFrequencia()).isEqualTo("PRESENTE");
        }

        @Test
        @DisplayName("Deve lançar BusinessException quando já existe frequência ativa para o mesmo aluno/aula")
        void deveLancarBusinessException_quandoFrequenciaDuplicada() {
            Pessoa aluno = new Pessoa();
            aluno.setIdPessoa(1L);
            Aula aula = new Aula();
            aula.setChamadaEncerrada(false);

            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
            when(repository.findByAula_IdAulaAndAluno_IdPessoa(1L, 1L))
                    .thenReturn(Optional.of(new AlunoFrequencia()));

            assertThatThrownBy(() -> service.registrar(1L, 1L, "FALTA"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("já registrada");

            verify(repository, never()).save(any());
        }
    }
}
