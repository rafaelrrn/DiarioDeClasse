package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.calendario.CalendarioEscolarRepository;
import com.diario.de.classe.modules.cronograma.PeriodoLetivoRepository;
import com.diario.de.classe.modules.turma.DisciplinaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pelo gerenciamento de avaliações.
 *
 * <p>Durante a transição (Etapa B → C), os campos {@code calendarioEscolar}
 * e {@code periodoLetivo} coexistem. O vínculo com {@code calendarioEscolar}
 * é mantido como nullable; o novo vínculo é {@code periodoLetivo}.
 */
@Service
public class AvaliacaoService {

    private final AvaliacaoRepository repository;
    private final DisciplinaRepository disciplinaRepository;
    private final CalendarioEscolarRepository calendarioEscolarRepository;
    private final PeriodoLetivoRepository periodoLetivoRepository;

    public AvaliacaoService(AvaliacaoRepository repository,
                            DisciplinaRepository disciplinaRepository,
                            CalendarioEscolarRepository calendarioEscolarRepository,
                            PeriodoLetivoRepository periodoLetivoRepository) {
        this.repository                = repository;
        this.disciplinaRepository      = disciplinaRepository;
        this.calendarioEscolarRepository = calendarioEscolarRepository;
        this.periodoLetivoRepository   = periodoLetivoRepository;
    }

    public List<Avaliacao> buscarTodos() {
        return repository.findAllAtivos();
    }

    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
    }

    /**
     * Cria uma nova avaliação.
     *
     * @param avaliacao           dados básicos (materia, dia, peso, tipo — já setados pelo controller)
     * @param idDisciplina        ID da disciplina (obrigatório)
     * @param idCalendarioEscolar ID do calendário legado (opcional — transição)
     * @param idPeriodoLetivo     ID do período letivo (opcional)
     */
    @Transactional
    public Avaliacao criar(Avaliacao avaliacao, Long idDisciplina, Long idCalendarioEscolar, Long idPeriodoLetivo) {
        avaliacao.setDisciplina(disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id: " + idDisciplina)));

        if (idCalendarioEscolar != null) {
            avaliacao.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        }

        if (idPeriodoLetivo != null) {
            avaliacao.setPeriodoLetivo(periodoLetivoRepository.findById(idPeriodoLetivo)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Período letivo não encontrado com id: " + idPeriodoLetivo)));
        }

        return repository.save(avaliacao);
    }

    /**
     * Atualiza os dados de uma avaliação existente.
     *
     * @param id                  ID da avaliação a atualizar
     * @param dados               novos dados (materia, dia, peso, tipo — já setados pelo controller)
     * @param idDisciplina        novo ID de disciplina (mantém atual se nulo)
     * @param idCalendarioEscolar novo ID de calendário legado (mantém atual se nulo)
     * @param idPeriodoLetivo     novo ID de período letivo (mantém atual se nulo)
     */
    @Transactional
    public Avaliacao atualizar(Long id, Avaliacao dados, Long idDisciplina, Long idCalendarioEscolar, Long idPeriodoLetivo) {
        Avaliacao existente = buscarPorId(id);
        existente.setMateria(dados.getMateria());
        existente.setDia(dados.getDia());
        existente.setPeso(dados.getPeso());
        existente.setTipo(dados.getTipo());

        if (idDisciplina != null) {
            existente.setDisciplina(disciplinaRepository.findById(idDisciplina)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Disciplina não encontrada com id: " + idDisciplina)));
        }
        if (idCalendarioEscolar != null) {
            existente.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        }
        if (idPeriodoLetivo != null) {
            existente.setPeriodoLetivo(periodoLetivoRepository.findById(idPeriodoLetivo)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Período letivo não encontrado com id: " + idPeriodoLetivo)));
        }

        return repository.save(existente);
    }

    @Transactional
    public void desativar(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setAtivo(false);
        avaliacao.setDeletedAt(LocalDateTime.now());
        repository.save(avaliacao);
    }
}
