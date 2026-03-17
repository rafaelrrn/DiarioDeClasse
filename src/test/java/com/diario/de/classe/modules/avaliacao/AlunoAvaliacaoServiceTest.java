package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.NotaLancamentoDTO;
import com.diario.de.classe.modules.frequencia.AlunoFrequenciaService;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do AlunoAvaliacaoService.
 *
 * Cobrem as regras de negócio definidas em INSTRUCTIONS.md §7.2:
 * - Média ponderada: Σ(nota * peso) / Σ(peso)
 * - Situação: APROVADO, EM_RECUPERACAO, REPROVADO_NOTA, REPROVADO_FREQUENCIA
 * - Lançamento em lote com validação de duplicidade
 */
@ExtendWith(MockitoExtension.class)
class AlunoAvaliacaoServiceTest {

    @Mock private AlunoAvaliacaoRepository repository;
    @Mock private PessoaRepository pessoaRepository;
    @Mock private AvaliacaoRepository avaliacaoRepository;
    @Mock private AlunoFrequenciaService alunoFrequenciaService;

    @InjectMocks
    private AlunoAvaliacaoService service;

    // -------------------------------------------------------------------------
    // calcularSituacao
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("calcularSituacao — regras de aprovação e reprovação")
    class CalcularSituacaoTest {

        @Test
        @DisplayName("Deve retornar APROVADO quando média >= 5.0 e frequência >= 75%")
        void deveRetornarAprovado_quandoMediaAltaEFrequenciaAdequada() {
            SituacaoAluno situacao = service.calcularSituacao(4.0, 80.0);
            assertThat(situacao).isEqualTo(SituacaoAluno.APROVADO);
        }

        @Test
        @DisplayName("Deve retornar APROVADO no limite exato: média = 5.0 e frequência = 75%")
        void deveRetornarAprovado_noLimiteExato() {
            SituacaoAluno situacao = service.calcularSituacao(7.0, 75.0);
            assertThat(situacao).isEqualTo(SituacaoAluno.APROVADO);
        }

        @Test
        @DisplayName("Deve retornar REPROVADO_FREQUENCIA quando frequência < 75%, independente da nota")
        void deveRetornarReprovadoFrequencia_quandoFrequenciaBaixa_mesmComNotaAlta() {
            // A frequência tem precedência sobre a nota — reprovação por LDB Art. 24
            SituacaoAluno situacao = service.calcularSituacao(9.0, 74.9);
            assertThat(situacao).isEqualTo(SituacaoAluno.REPROVADO_FREQUENCIA);
        }

        @Test
        @DisplayName("Deve retornar EM_RECUPERACAO quando média entre 3.0 e 4.9 (inclusive)")
        void deveRetornarEmRecuperacao_quandoMediaEntre3e5() {
            assertThat(service.calcularSituacao(3.0, 80.0)).isEqualTo(SituacaoAluno.EM_RECUPERACAO);
            assertThat(service.calcularSituacao(4.5, 80.0)).isEqualTo(SituacaoAluno.EM_RECUPERACAO);
            assertThat(service.calcularSituacao(4.99, 80.0)).isEqualTo(SituacaoAluno.EM_RECUPERACAO);
        }

        @Test
        @DisplayName("Deve retornar REPROVADO_NOTA quando média < 3.0 (reprovação direta)")
        void deveRetornarReprovadoNota_quandoMediaAbaixo3() {
            // Abaixo de 3.0 é reprovação direta sem direito a recuperação (regra da instituição)
            assertThat(service.calcularSituacao(2.9, 80.0)).isEqualTo(SituacaoAluno.REPROVADO_NOTA);
            assertThat(service.calcularSituacao(0.0, 80.0)).isEqualTo(SituacaoAluno.REPROVADO_NOTA);
        }
    }

    // -------------------------------------------------------------------------
    // calcularMediaPonderada
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("calcularMediaPonderada — fórmula Σ(nota * peso) / Σ(peso)")
    class CalcularMediaPonderadaTest {

        @Test
        @DisplayName("Deve calcular a média ponderada corretamente com pesos diferentes")
        void deveCalcularMediaPonderada_comPesosDiferentes() {
            // Cenário: prova (peso 7) nota 8 + trabalho (peso 3) nota 6
            // Esperado: (8*7 + 6*3) / (7+3) = (56 + 18) / 10 = 7.4
            Avaliacao prova = new Avaliacao();
            prova.setPeso(7);
            Avaliacao trabalho = new Avaliacao();
            trabalho.setPeso(3);

            AlunoAvaliacao nota1 = new AlunoAvaliacao();
            nota1.setNota(8.0f);
            nota1.setAvaliacao(prova);

            AlunoAvaliacao nota2 = new AlunoAvaliacao();
            nota2.setNota(6.0f);
            nota2.setAvaliacao(trabalho);

            when(repository.findAtivasByAlunoAndDisciplina(1L, 1L))
                    .thenReturn(List.of(nota1, nota2));

            double media = service.calcularMediaPonderada(1L, 1L);

            assertThat(media).isEqualTo(7.4);
        }

        @Test
        @DisplayName("Deve tratar peso nulo como peso 1 (todas as avaliações com o mesmo valor)")
        void deveAssumirPeso1_quandoPesoNulo() {
            // Cenário: duas avaliações sem peso → média simples
            // nota 8 e nota 6 → média = (8 + 6) / 2 = 7.0
            Avaliacao av1 = new Avaliacao(); // peso = null
            Avaliacao av2 = new Avaliacao(); // peso = null

            AlunoAvaliacao nota1 = new AlunoAvaliacao();
            nota1.setNota(8.0f);
            nota1.setAvaliacao(av1);

            AlunoAvaliacao nota2 = new AlunoAvaliacao();
            nota2.setNota(6.0f);
            nota2.setAvaliacao(av2);

            when(repository.findAtivasByAlunoAndDisciplina(1L, 1L))
                    .thenReturn(List.of(nota1, nota2));

            double media = service.calcularMediaPonderada(1L, 1L);

            assertThat(media).isEqualTo(7.0);
        }

        @Test
        @DisplayName("Deve retornar 0.0 quando aluno não tem notas na disciplina")
        void deveRetornarZero_quandoSemNotas() {
            when(repository.findAtivasByAlunoAndDisciplina(1L, 1L))
                    .thenReturn(List.of());

            double media = service.calcularMediaPonderada(1L, 1L);

            assertThat(media).isEqualTo(0.0);
        }
    }

    // -------------------------------------------------------------------------
    // lancarNotasEmLote
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("lancarNotasEmLote — lançamento transacional de notas")
    class LancarNotasEmLoteTest {

        @Test
        @DisplayName("Deve lançar notas para todos os alunos da lista")
        void deveLancarNotas_quandoDadosValidos() {
            Avaliacao avaliacao = new Avaliacao();
            Pessoa aluno = new Pessoa();
            aluno.setIdPessoa(1L);

            AlunoAvaliacao notaSalva = new AlunoAvaliacao();
            notaSalva.setNota(8.5f);

            when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            when(repository.existsByPessoaAlunoIdPessoaAndAvaliacaoIdAvaliacaoAndAtivoTrue(1L, 1L))
                    .thenReturn(false);
            when(repository.save(any(AlunoAvaliacao.class))).thenReturn(notaSalva);

            List<NotaLancamentoDTO> notas = List.of(new NotaLancamentoDTO(1L, 8.5f, null));
            List<AlunoAvaliacao> resultado = service.lancarNotasEmLote(1L, notas);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).save(any(AlunoAvaliacao.class));
        }

        @Test
        @DisplayName("Deve lançar BusinessException quando aluno já tem nota ativa nesta avaliação")
        void deveLancarBusinessException_quandoNotaDuplicada() {
            Avaliacao avaliacao = new Avaliacao();
            Pessoa aluno = new Pessoa();
            aluno.setIdPessoa(1L);

            when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(aluno));
            // Simula nota já existente para este aluno/avaliação
            when(repository.existsByPessoaAlunoIdPessoaAndAvaliacaoIdAvaliacaoAndAtivoTrue(1L, 1L))
                    .thenReturn(true);

            List<NotaLancamentoDTO> notas = List.of(new NotaLancamentoDTO(1L, 8.5f, null));

            assertThatThrownBy(() -> service.lancarNotasEmLote(1L, notas))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("já possui nota");

            // Garante que nenhum save foi chamado após a detecção da duplicidade
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando avaliação não existe")
        void deveLancarResourceNotFoundException_quandoAvaliacaoNaoExiste() {
            when(avaliacaoRepository.findById(99L)).thenReturn(Optional.empty());

            List<NotaLancamentoDTO> notas = List.of(new NotaLancamentoDTO(1L, 8.5f, null));

            assertThatThrownBy(() -> service.lancarNotasEmLote(99L, notas))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Avaliação não encontrada");
        }
    }
}
