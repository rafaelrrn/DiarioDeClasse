package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.calendario.CalendarioEscolarRepository;
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
 */
@Service
public class AlunoFrequenciaService {

    private final AlunoFrequenciaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final CalendarioEscolarRepository calendarioEscolarRepository;
    private final AlunoTurmaRepository alunoTurmaRepository;

    public AlunoFrequenciaService(AlunoFrequenciaRepository repository,
                                  PessoaRepository pessoaRepository,
                                  CalendarioEscolarRepository calendarioEscolarRepository,
                                  AlunoTurmaRepository alunoTurmaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.calendarioEscolarRepository = calendarioEscolarRepository;
        this.alunoTurmaRepository = alunoTurmaRepository;
    }

    // -------------------------------------------------------------------------
    // Consultas básicas
    // -------------------------------------------------------------------------

    /**
     * Lista todos os registros de frequência ativos (exclui soft-deleted).
     *
     * @return lista de frequências ativas
     */
    public List<AlunoFrequencia> buscarTodos() {
        return repository.findAllAtivos();
    }

    /**
     * Busca um registro de frequência pelo ID.
     *
     * @param id ID do registro
     * @return entidade encontrada
     * @throws ResourceNotFoundException se não encontrado ou inativo
     */
    public AlunoFrequencia buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frequência não encontrada com id: " + id));
    }

    /**
     * Lista todas as frequências ativas de um aluno.
     *
     * @param idAluno ID da Pessoa com tipo ALUNO
     * @return lista de frequências do aluno
     */
    public List<AlunoFrequencia> buscarPorAluno(Long idAluno) {
        return repository.findAtivosByAluno(idAluno);
    }

    // -------------------------------------------------------------------------
    // Lançamento de frequência
    // -------------------------------------------------------------------------

    /**
     * Registra a frequência de um único aluno em uma aula.
     *
     * <p>Valida:
     * <ul>
     *   <li>Existência do aluno e do calendário escolar.</li>
     *   <li>Duplicidade: impede dois registros ativos para o mesmo aluno/aula.</li>
     * </ul>
     *
     * @param idAluno             ID do aluno
     * @param idCalendarioEscolar ID da aula (calendário escolar)
     * @param tipo                tipo de frequência; se nulo, assume {@link TipoFrequencia#PRESENTE}
     * @return registro de frequência criado
     * @throws BusinessException se já existe frequência ativa para esse aluno nesta aula
     */
    @Transactional
    public AlunoFrequencia registrar(Long idAluno, Long idCalendarioEscolar, TipoFrequencia tipo) {
        // Valida aluno
        var aluno = pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno));

        // Valida calendário
        var calendario = calendarioEscolarRepository.findById(idCalendarioEscolar)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calendário escolar não encontrado com id: " + idCalendarioEscolar));

        // Impede lançamento duplicado para o mesmo aluno/aula
        if (repository.existsByPessoaAlunoIdPessoaAndCalendarioEscolarIdCalendarioEscolarAndAtivoTrue(
                idAluno, idCalendarioEscolar)) {
            throw new BusinessException(
                    "Frequência já registrada para este aluno nesta aula. " +
                    "Use PUT /v1/frequencias/{id} para corrigir.");
        }

        AlunoFrequencia frequencia = new AlunoFrequencia();
        frequencia.setPessoaAluno(aluno);
        frequencia.setCalendarioEscolar(calendario);
        // Assume PRESENTE quando o tipo não for informado (lançamento padrão)
        frequencia.setTipoFrequencia(tipo != null ? tipo : TipoFrequencia.PRESENTE);

        return repository.save(frequencia);
    }

    /**
     * Lança frequência em lote para todos os alunos matriculados em uma turma.
     *
     * <p>Útil para o professor registrar a chamada de uma aula inteira de uma vez.
     * Para cada aluno ativo da turma:
     * <ul>
     *   <li>Usa o tipo informado no mapa {@code tiposPorAluno} se existir.</li>
     *   <li>Usa {@code tipoPadrao} para alunos não mapeados explicitamente.</li>
     *   <li>Ignora alunos que já possuem frequência ativa para esta aula
     *       (não lança exceção — apenas pula).</li>
     * </ul>
     *
     * @param idTurma             ID da turma
     * @param idCalendarioEscolar ID da aula (calendário escolar)
     * @param tiposPorAluno       mapa opcional de {@code idAluno → TipoFrequencia}
     * @param tipoPadrao          tipo usado quando o aluno não está no mapa; padrão: PRESENTE
     * @return lista de registros criados neste lançamento
     */
    @Transactional
    public List<AlunoFrequencia> registrarTurma(Long idTurma,
                                                Long idCalendarioEscolar,
                                                Map<Long, TipoFrequencia> tiposPorAluno,
                                                TipoFrequencia tipoPadrao) {
        // Valida o calendário escolar
        var calendario = calendarioEscolarRepository.findById(idCalendarioEscolar)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calendário escolar não encontrado com id: " + idCalendarioEscolar));

        // Busca todos os alunos ativamente matriculados na turma
        List<AlunoTurma> matriculas = alunoTurmaRepository.findAtivosByTurmaId(idTurma);
        if (matriculas.isEmpty()) {
            throw new BusinessException("Nenhum aluno matriculado na turma com id: " + idTurma);
        }

        TipoFrequencia padrao = tipoPadrao != null ? tipoPadrao : TipoFrequencia.PRESENTE;
        Map<Long, TipoFrequencia> tipos = tiposPorAluno != null ? tiposPorAluno : Map.of();

        return matriculas.stream()
                .filter(m -> !repository.existsByPessoaAlunoIdPessoaAndCalendarioEscolarIdCalendarioEscolarAndAtivoTrue(
                        m.getPessoaAluno().getIdPessoa(), idCalendarioEscolar))
                .map(m -> {
                    AlunoFrequencia f = new AlunoFrequencia();
                    f.setPessoaAluno(m.getPessoaAluno());
                    f.setCalendarioEscolar(calendario);
                    f.setTipoFrequencia(tipos.getOrDefault(m.getPessoaAluno().getIdPessoa(), padrao));
                    return repository.save(f);
                })
                .toList();
    }

    /**
     * Atualiza o tipo de frequência de um registro existente.
     *
     * <p>Permite ao professor corrigir um lançamento errado (ex: marcou FALTA, era PRESENTE).
     *
     * @param id   ID do registro de frequência
     * @param tipo novo tipo de frequência
     * @return registro atualizado
     */
    @Transactional
    public AlunoFrequencia atualizar(Long id, TipoFrequencia tipo) {
        AlunoFrequencia existente = buscarPorId(id);
        existente.setTipoFrequencia(tipo);
        return repository.save(existente);
    }

    /**
     * Desativa (soft delete) um registro de frequência.
     *
     * <p>O registro permanece no banco com {@code ativo = false} para preservar
     * o histórico pedagógico. Após desativar, o lançamento pode ser refeito.
     *
     * @param id ID do registro a desativar
     */
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
     * <p>Fórmula (LDB Art. 24):
     * {@code percentual = (totalPresencas / totalAulas) * 100}
     *
     * <p>Considera FALTA_JUSTIFICADA no denominador (aula ocorreu), mas
     * não computa como falta para efeito de reprovação.
     *
     * @param idAluno ID do aluno
     * @return DTO com contagens, percentual e indicador de risco (< 75%)
     */
    public FrequenciaResumoDTO calcularFrequencia(Long idAluno) {
        // Verifica existência do aluno
        if (!pessoaRepository.existsById(idAluno)) {
            throw new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno);
        }

        long totalAulas     = repository.countTotalAulasByAluno(idAluno);
        long presencas      = repository.countByAlunoAndTipo(idAluno, TipoFrequencia.PRESENTE);
        long faltas         = repository.countByAlunoAndTipo(idAluno, TipoFrequencia.FALTA);
        long faltasJust     = repository.countByAlunoAndTipo(idAluno, TipoFrequencia.FALTA_JUSTIFICADA);

        // Evita divisão por zero quando nenhuma aula foi registrada
        double percentual = totalAulas > 0
                ? ((double) presencas / totalAulas) * 100.0
                : 0.0;

        boolean emRisco = percentual < FrequenciaResumoDTO.FREQUENCIA_MINIMA_LDB;

        return new FrequenciaResumoDTO(idAluno, totalAulas, presencas, faltas, faltasJust, percentual, emRisco);
    }
}
