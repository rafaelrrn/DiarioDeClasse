package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.avaliacao.dto.BoletimResponseDTO;
import com.diario.de.classe.modules.avaliacao.dto.MediaDisciplinaDTO;
import com.diario.de.classe.modules.avaliacao.dto.NotaLancamentoDTO;
import com.diario.de.classe.modules.frequencia.AlunoFrequenciaService;
import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo lançamento e consulta de notas de alunos.
 *
 * <p>Regras de negócio implementadas (INSTRUCTIONS.md §7.2):
 * <ul>
 *   <li>Média ponderada: {@code Σ(nota * peso) / Σ(peso)}</li>
 *   <li>Quando a avaliação não tem peso definido, assume peso 1.</li>
 *   <li>Situação do aluno:
 *     <ul>
 *       <li>APROVADO: média ≥ 5.0 e frequência ≥ 75%</li>
 *       <li>EM_RECUPERACAO: média entre 3.0 e 4.9</li>
 *       <li>REPROVADO_NOTA: média < 3.0 (reprovação direta)</li>
 *       <li>REPROVADO_FREQUENCIA: frequência < 75% (LDB Art. 24)</li>
 *     </ul>
 *   </li>
 * </ul>
 */
@Service
public class AlunoAvaliacaoService {

    private final AlunoAvaliacaoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final AlunoFrequenciaService alunoFrequenciaService;

    public AlunoAvaliacaoService(AlunoAvaliacaoRepository repository,
                                 PessoaRepository pessoaRepository,
                                 AvaliacaoRepository avaliacaoRepository,
                                 AlunoFrequenciaService alunoFrequenciaService) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.alunoFrequenciaService = alunoFrequenciaService;
    }

    // -------------------------------------------------------------------------
    // Consultas básicas
    // -------------------------------------------------------------------------

    /**
     * Lista todas as notas ativas (exclui soft-deleted).
     *
     * @return lista de notas ativas
     */
    public List<AlunoAvaliacao> buscarTodos() {
        return repository.findAllAtivos();
    }

    /**
     * Busca uma nota pelo ID.
     *
     * @param id ID da nota
     * @return entidade encontrada
     * @throws ResourceNotFoundException se não existir
     */
    public AlunoAvaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nota de aluno não encontrada com id: " + id));
    }

    /**
     * Lista todas as notas ativas de um aluno.
     *
     * @param idAluno ID do aluno
     * @return lista de notas do aluno
     * @throws ResourceNotFoundException se o aluno não existir
     */
    public List<AlunoAvaliacao> buscarPorAluno(Long idAluno) {
        if (!pessoaRepository.existsById(idAluno)) {
            throw new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno);
        }
        return repository.findAtivasByAluno(idAluno);
    }

    // -------------------------------------------------------------------------
    // Lançamento de notas
    // -------------------------------------------------------------------------

    /**
     * Registra a nota de um único aluno em uma avaliação.
     *
     * <p>Impede lançamento duplicado: se já existe nota ativa para este aluno
     * nesta avaliação, lança {@link BusinessException}.
     *
     * @param idAluno     ID do aluno
     * @param idAvaliacao ID da avaliação
     * @param nota        Nota de 0.0 a 10.0
     * @param obs         Observação opcional
     * @return registro de nota criado
     * @throws BusinessException se já existe nota ativa para esse aluno nesta avaliação
     */
    @Transactional
    public AlunoAvaliacao registrar(Long idAluno, Long idAvaliacao, Float nota, String obs) {
        Pessoa aluno = pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno));

        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + idAvaliacao));

        // Impede nota duplicada para o mesmo aluno/avaliação
        if (repository.existsByPessoaAlunoIdPessoaAndAvaliacaoIdAvaliacaoAndAtivoTrue(idAluno, idAvaliacao)) {
            throw new BusinessException(
                    "Já existe nota lançada para este aluno nesta avaliação. " +
                    "Use PUT /v1/alunos-avaliacao/{id} para corrigir.");
        }

        AlunoAvaliacao alunoAvaliacao = new AlunoAvaliacao();
        alunoAvaliacao.setPessoaAluno(aluno);
        alunoAvaliacao.setAvaliacao(avaliacao);
        alunoAvaliacao.setNota(nota);
        alunoAvaliacao.setObs(obs);

        return repository.save(alunoAvaliacao);
    }

    /**
     * Lança notas em lote para todos os alunos de uma avaliação.
     *
     * <p>Para cada entrada da lista:
     * <ul>
     *   <li>Se já existe nota ativa para o aluno nesta avaliação → lança {@link BusinessException}.</li>
     *   <li>Caso contrário, cria o registro normalmente.</li>
     * </ul>
     *
     * <p>Toda a operação é transacional: se um lançamento falhar, nenhum é salvo.
     *
     * @param idAvaliacao ID da avaliação alvo
     * @param notas       Lista com idAluno, nota e obs de cada aluno
     * @return lista de registros criados
     * @throws ResourceNotFoundException se a avaliação não existir
     * @throws BusinessException         se algum aluno já tiver nota ativa nesta avaliação
     */
    @Transactional
    public List<AlunoAvaliacao> lancarNotasEmLote(Long idAvaliacao, List<NotaLancamentoDTO> notas) {
        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + idAvaliacao));

        return notas.stream().map(dto -> {
            Pessoa aluno = pessoaRepository.findById(dto.idAluno())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Aluno não encontrado com id: " + dto.idAluno()));

            // Verifica duplicidade antes de salvar — falha rápida sem persistir nada
            if (repository.existsByPessoaAlunoIdPessoaAndAvaliacaoIdAvaliacaoAndAtivoTrue(
                    dto.idAluno(), idAvaliacao)) {
                throw new BusinessException(
                        "Aluno id=" + dto.idAluno() + " já possui nota nesta avaliação.");
            }

            AlunoAvaliacao aa = new AlunoAvaliacao();
            aa.setPessoaAluno(aluno);
            aa.setAvaliacao(avaliacao);
            aa.setNota(dto.nota());
            aa.setObs(dto.obs());
            return repository.save(aa);
        }).toList();
    }

    /**
     * Atualiza a nota de um registro existente.
     *
     * <p>Permite ao professor corrigir uma nota lançada incorretamente.
     *
     * @param id   ID do registro de nota
     * @param nota nova nota (0.0 – 10.0)
     * @param obs  nova observação (pode ser nula para manter a anterior)
     * @return registro atualizado
     */
    @Transactional
    public AlunoAvaliacao atualizar(Long id, Float nota, String obs) {
        AlunoAvaliacao existente = buscarPorId(id);
        existente.setNota(nota);
        if (obs != null) {
            existente.setObs(obs);
        }
        return repository.save(existente);
    }

    /**
     * Desativa (soft delete) um registro de nota.
     *
     * <p>O registro permanece no banco com {@code ativo = false} para preservar
     * o histórico. Após desativar, uma nova nota pode ser lançada para o mesmo aluno/avaliação.
     *
     * @param id ID do registro a desativar
     */
    @Transactional
    public void desativar(Long id) {
        AlunoAvaliacao nota = buscarPorId(id);
        nota.setAtivo(false);
        nota.setDeletedAt(LocalDateTime.now());
        repository.save(nota);
    }

    // -------------------------------------------------------------------------
    // Cálculo de média ponderada e situação do aluno
    // -------------------------------------------------------------------------

    /**
     * Calcula a média ponderada de um aluno em uma disciplina específica.
     *
     * <p>Fórmula: {@code média = Σ(nota * peso) / Σ(peso)}
     * <br>Quando a avaliação não possui {@code peso} definido, assume peso 1.
     * <br>Retorna 0.0 se não há notas ativas para o aluno nesta disciplina.
     *
     * @param idAluno      ID do aluno
     * @param idDisciplina ID da disciplina
     * @return média ponderada calculada (0.0 – 10.0)
     */
    public double calcularMediaPonderada(Long idAluno, Long idDisciplina) {
        List<AlunoAvaliacao> notas = repository.findAtivasByAlunoAndDisciplina(idAluno, idDisciplina);

        if (notas.isEmpty()) return 0.0;

        // Numerador: Σ(nota * peso)
        double numerador = notas.stream()
                .mapToDouble(aa -> {
                    int peso = aa.getAvaliacao().getPeso() != null ? aa.getAvaliacao().getPeso() : 1;
                    return aa.getNota() * peso;
                })
                .sum();

        // Denominador: Σ(peso)
        int denominador = notas.stream()
                .mapToInt(aa -> aa.getAvaliacao().getPeso() != null ? aa.getAvaliacao().getPeso() : 1)
                .sum();

        return denominador > 0 ? numerador / denominador : 0.0;
    }

    /**
     * Determina a situação do aluno com base na média e na frequência.
     *
     * <p>Regras (em ordem de precedência):
     * <ol>
     *   <li>Frequência < 75% → {@link SituacaoAluno#REPROVADO_FREQUENCIA} (LDB Art. 24)</li>
     *   <li>Média ≥ 5.0 → {@link SituacaoAluno#APROVADO}</li>
     *   <li>Média entre 3.0 e 4.9 → {@link SituacaoAluno#EM_RECUPERACAO}</li>
     *   <li>Média < 3.0 → {@link SituacaoAluno#REPROVADO_NOTA} (reprovação direta)</li>
     * </ol>
     *
     * @param media                  média ponderada do aluno na disciplina
     * @param percentualFrequencia   percentual de presença (0.0 – 100.0)
     * @return situação calculada
     */
    public SituacaoAluno calcularSituacao(double media, double percentualFrequencia) {
        // Reprovação por frequência tem precedência sobre a nota (LDB Art. 24)
        if (percentualFrequencia < FrequenciaResumoDTO.FREQUENCIA_MINIMA_LDB) {
            return SituacaoAluno.REPROVADO_FREQUENCIA;
        }

        if (media >= 5.0) {
            return SituacaoAluno.APROVADO;
        }

        // Média de recuperação: entre 3.0 e 4.9 o aluno vai para recuperação.
        // Abaixo de 3.0 é reprovação direta, sem recuperação (regra da instituição).
        if (media >= 3.0) {
            return SituacaoAluno.EM_RECUPERACAO;
        }

        return SituacaoAluno.REPROVADO_NOTA;
    }

    // -------------------------------------------------------------------------
    // Boletim
    // -------------------------------------------------------------------------

    /**
     * Gera o boletim completo de um aluno consolidando notas e frequência.
     *
     * <p>Para cada disciplina em que o aluno tem notas ativas:
     * <ol>
     *   <li>Calcula a média ponderada.</li>
     *   <li>Consulta o percentual de frequência global do aluno.</li>
     *   <li>Determina a situação ({@link SituacaoAluno}).</li>
     * </ol>
     *
     * @param idAluno ID do aluno
     * @return boletim com frequência e médias por disciplina
     * @throws ResourceNotFoundException se o aluno não existir
     */
    public BoletimResponseDTO gerarBoletim(Long idAluno) {
        Pessoa aluno = pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno));

        // Busca todas as notas ativas do aluno
        List<AlunoAvaliacao> todasNotas = repository.findAtivasByAluno(idAluno);

        // Calcula frequência global do aluno para determinar situação
        FrequenciaResumoDTO frequencia = alunoFrequenciaService.calcularFrequencia(idAluno);

        // Agrupa as notas por disciplina para calcular a média ponderada de cada uma
        Map<Long, List<AlunoAvaliacao>> notasPorDisciplina = todasNotas.stream()
                .collect(Collectors.groupingBy(
                        aa -> aa.getAvaliacao().getDisciplina().getIdDisciplina()));

        List<MediaDisciplinaDTO> disciplinas = notasPorDisciplina.entrySet().stream()
                .map(entry -> {
                    Long idDisciplina = entry.getKey();
                    List<AlunoAvaliacao> notas = entry.getValue();

                    String nomeDisciplina = notas.get(0).getAvaliacao().getDisciplina().getNome();

                    // Calcula média ponderada: Σ(nota * peso) / Σ(peso)
                    double numerador = notas.stream()
                            .mapToDouble(aa -> {
                                int peso = aa.getAvaliacao().getPeso() != null ? aa.getAvaliacao().getPeso() : 1;
                                return aa.getNota() * peso;
                            })
                            .sum();
                    int denominador = notas.stream()
                            .mapToInt(aa -> aa.getAvaliacao().getPeso() != null ? aa.getAvaliacao().getPeso() : 1)
                            .sum();
                    double media = denominador > 0 ? numerador / denominador : 0.0;

                    SituacaoAluno situacao = calcularSituacao(media, frequencia.percentualPresenca());

                    return new MediaDisciplinaDTO(idDisciplina, nomeDisciplina, media, situacao);
                })
                .toList();

        return new BoletimResponseDTO(idAluno, aluno.getNome(), frequencia, disciplinas);
    }
}
