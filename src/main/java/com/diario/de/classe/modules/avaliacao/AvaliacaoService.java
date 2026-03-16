package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.calendario.CalendarioEscolarRepository;
import com.diario.de.classe.modules.turma.DisciplinaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pelo gerenciamento de avaliações.
 *
 * <p>Uma avaliação define o instrumento de medição (prova, trabalho, etc.),
 * vinculado a uma disciplina e opcionalmente a um calendário escolar.
 * O campo {@code peso} determina o quanto essa avaliação vale na média ponderada.
 */
@Service
public class AvaliacaoService {

    private final AvaliacaoRepository repository;
    private final DisciplinaRepository disciplinaRepository;
    private final CalendarioEscolarRepository calendarioEscolarRepository;

    public AvaliacaoService(AvaliacaoRepository repository,
                            DisciplinaRepository disciplinaRepository,
                            CalendarioEscolarRepository calendarioEscolarRepository) {
        this.repository = repository;
        this.disciplinaRepository = disciplinaRepository;
        this.calendarioEscolarRepository = calendarioEscolarRepository;
    }

    /**
     * Lista todas as avaliações ativas (exclui soft-deleted).
     *
     * @return lista de avaliações ativas
     */
    public List<Avaliacao> buscarTodos() {
        return repository.findAllAtivos();
    }

    /**
     * Busca uma avaliação pelo ID.
     *
     * @param id ID da avaliação
     * @return entidade encontrada
     * @throws ResourceNotFoundException se não existir
     */
    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
    }

    /**
     * Cria uma nova avaliação vinculada a uma disciplina e opcionalmente a um calendário.
     *
     * @param avaliacao           dados da avaliação (materia, dia, peso)
     * @param idDisciplina        ID da disciplina obrigatória
     * @param idCalendarioEscolar ID do calendário escolar (opcional)
     * @return avaliação criada e persistida
     * @throws ResourceNotFoundException se a disciplina ou calendário não forem encontrados
     */
    @Transactional
    public Avaliacao criar(Avaliacao avaliacao, Long idDisciplina, Long idCalendarioEscolar) {
        avaliacao.setDisciplina(disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id: " + idDisciplina)));

        if (idCalendarioEscolar != null) {
            avaliacao.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        }

        return repository.save(avaliacao);
    }

    /**
     * Atualiza os dados de uma avaliação existente.
     *
     * @param id                  ID da avaliação a atualizar
     * @param dados               novos dados (materia, dia, peso)
     * @param idDisciplina        novo ID de disciplina (opcional — mantém a atual se nulo)
     * @param idCalendarioEscolar novo ID de calendário (opcional — mantém o atual se nulo)
     * @return avaliação atualizada
     */
    @Transactional
    public Avaliacao atualizar(Long id, Avaliacao dados, Long idDisciplina, Long idCalendarioEscolar) {
        Avaliacao existente = buscarPorId(id);
        existente.setMateria(dados.getMateria());
        existente.setDia(dados.getDia());
        existente.setPeso(dados.getPeso());

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

        return repository.save(existente);
    }

    /**
     * Desativa (soft delete) uma avaliação.
     *
     * <p>O registro permanece no banco para preservar o histórico pedagógico —
     * as notas vinculadas a esta avaliação continuam acessíveis.
     *
     * @param id ID da avaliação a desativar
     */
    @Transactional
    public void desativar(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setAtivo(false);
        avaliacao.setDeletedAt(LocalDateTime.now());
        repository.save(avaliacao);
    }
}
