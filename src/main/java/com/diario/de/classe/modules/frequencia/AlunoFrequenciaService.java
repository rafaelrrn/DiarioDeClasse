package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.cronograma.Aula;
import com.diario.de.classe.modules.cronograma.AulaRepository;
import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.modules.turma.AlunoTurma;
import com.diario.de.classe.modules.turma.AlunoTurmaRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Serviço responsável pelo lançamento e consulta de frequência de alunos.
 *
 * <p>Regras da LDB (Lei nº 9.394/96, Art. 24):
 * <ul>
 *   <li>Frequência mínima: 75% do total de horas/aulas.</li>
 *   <li>FALTA_JUSTIFICADA não conta para reprovação, mas a aula ainda ocorreu.</li>
 *   <li>Fórmula: {@code percentual = (totalPresencas / totalAulas) * 100}</li>
 * </ul>
 *
 * <p>A API recebe e retorna {@code tipoFrequencia} como String.
 * Internamente o enum {@link TipoFrequencia} é usado para validação
 * e para o método {@link TipoFrequencia#contaParaReprovacao()}.
 */
@Service
public class AlunoFrequenciaService {

    private final AlunoFrequenciaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final AulaRepository aulaRepository;
    private final AlunoTurmaRepository alunoTurmaRepository;

    public AlunoFrequenciaService(AlunoFrequenciaRepository repository,
                                  PessoaRepository pessoaRepository,
                                  AulaRepository aulaRepository,
                                  AlunoTurmaRepository alunoTurmaRepository) {
        this.repository          = repository;
        this.pessoaRepository    = pessoaRepository;
        this.aulaRepository      = aulaRepository;
        this.alunoTurmaRepository = alunoTurmaRepository;
    }

    // -------------------------------------------------------------------------
    // Consultas básicas
    // -------------------------------------------------------------------------

    public List<AlunoFrequencia> buscarTodos() {
        return repository.findAllByAtivoTrue();
    }

    public AlunoFrequencia buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frequência não encontrada com id: " + id));
    }

    public List<AlunoFrequencia> buscarPorAluno(Long idAluno) {
        return repository.findByAluno_IdPessoaAndAtivoTrue(idAluno);
    }

    // -------------------------------------------------------------------------
    // Lançamento de frequência
    // -------------------------------------------------------------------------

    /**
     * Registra a frequência de um único aluno em uma aula.
     *
     * @param idAluno ID do aluno (Pessoa tipo ALUNO)
     * @param idAula  ID da aula
     * @param tipo    tipo como String: PRESENTE, FALTA ou FALTA_JUSTIFICADA; nulo assume PRESENTE
     */
    @Transactional
    public AlunoFrequencia registrar(Long idAluno, Long idAula, String tipo) {
        var aluno = pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno));

        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + idAula));

        if (Boolean.TRUE.equals(aula.getChamadaEncerrada())) {
            throw new BusinessException("A chamada desta aula já foi encerrada e não pode ser alterada.");
        }

        if (repository.findByAula_IdAulaAndAluno_IdPessoa(idAula, idAluno).isPresent()) {
            throw new BusinessException(
                    "Frequência já registrada para este aluno nesta aula. " +
                    "Use PUT /v1/frequencias/{id} para corrigir.");
        }

        // Valida o valor do tipo antes de persistir; usa o enum apenas para validação e domínio
        TipoFrequencia tipoEnum = TipoFrequencia.valueOf(tipo != null ? tipo : "PRESENTE");

        AlunoFrequencia frequencia = new AlunoFrequencia();
        frequencia.setAluno(aluno);
        frequencia.setAula(aula);
        frequencia.setTipoFrequencia(tipoEnum.name());

        return repository.save(frequencia);
    }

    /**
     * Lança frequência em lote para todos os alunos matriculados em uma turma.
     *
     * @param idTurma       ID da turma
     * @param idAula        ID da aula
     * @param tiposPorAluno mapa opcional de idAluno → tipo como String
     * @param tipoPadrao    tipo para alunos não mapeados (padrão: "PRESENTE")
     */
    @Transactional
    public List<AlunoFrequencia> registrarTurma(Long idTurma,
                                                Long idAula,
                                                Map<Long, String> tiposPorAluno,
                                                String tipoPadrao) {
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + idAula));

        if (Boolean.TRUE.equals(aula.getChamadaEncerrada())) {
            throw new BusinessException("A chamada desta aula já foi encerrada e não pode ser alterada.");
        }

        List<AlunoTurma> matriculas = alunoTurmaRepository.findAtivosByTurmaId(idTurma);
        if (matriculas.isEmpty()) {
            throw new BusinessException("Nenhum aluno matriculado na turma com id: " + idTurma);
        }

        // Valida e normaliza o tipo padrão
        String padrao = TipoFrequencia.valueOf(tipoPadrao != null ? tipoPadrao : "PRESENTE").name();
        Map<Long, String> tipos = tiposPorAluno != null ? tiposPorAluno : Map.of();

        return matriculas.stream()
                .filter(m -> repository.findByAula_IdAulaAndAluno_IdPessoa(
                        idAula, m.getPessoaAluno().getIdPessoa()).isEmpty())
                .map(m -> {
                    // Valida e normaliza o tipo de cada aluno
                    String tipoAluno = TipoFrequencia.valueOf(
                            tipos.getOrDefault(m.getPessoaAluno().getIdPessoa(), padrao)).name();
                    AlunoFrequencia f = new AlunoFrequencia();
                    f.setAluno(m.getPessoaAluno());
                    f.setAula(aula);
                    f.setTipoFrequencia(tipoAluno);
                    return repository.save(f);
                })
                .toList();
    }

    /**
     * Atualiza o tipo de frequência de um registro existente.
     *
     * @param id   ID do registro
     * @param tipo novo tipo como String (PRESENTE, FALTA, FALTA_JUSTIFICADA)
     */
    @Transactional
    public AlunoFrequencia atualizar(Long id, String tipo) {
        // Valida o valor via enum antes de persistir
        TipoFrequencia.valueOf(tipo);
        AlunoFrequencia existente = buscarPorId(id);
        existente.setTipoFrequencia(tipo);
        return repository.save(existente);
    }

    @Transactional
    public void desativar(Long id) {
        AlunoFrequencia frequencia = buscarPorId(id);
        frequencia.setAtivo(false);
        frequencia.setDeletedAt(LocalDateTime.now());
        repository.save(frequencia);
    }

    // -------------------------------------------------------------------------
    // Cálculo de frequência (regra LDB)
    // -------------------------------------------------------------------------

    /**
     * Calcula o resumo de frequência de um aluno com o percentual de presença.
     *
     * <p>Fórmula (LDB Art. 24): {@code percentual = (totalPresencas / totalAulas) * 100}
     */
    public FrequenciaResumoDTO calcularFrequencia(Long idAluno) {
        if (!pessoaRepository.existsById(idAluno)) {
            throw new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno);
        }

        long totalAulas  = repository.countByAluno_IdPessoaAndAtivoTrue(idAluno);
        long presencas   = repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(idAluno, TipoFrequencia.PRESENTE.name());
        long faltas      = repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(idAluno, TipoFrequencia.FALTA.name());
        long faltasJust  = repository.countByAluno_IdPessoaAndTipoFrequenciaAndAtivoTrue(idAluno, TipoFrequencia.FALTA_JUSTIFICADA.name());

        double percentual = totalAulas > 0
                ? ((double) presencas / totalAulas) * 100.0
                : 0.0;

        boolean emRisco = percentual < FrequenciaResumoDTO.FREQUENCIA_MINIMA_LDB;

        return new FrequenciaResumoDTO(idAluno, totalAulas, presencas, faltas, faltasJust, percentual, emRisco);
    }
}
