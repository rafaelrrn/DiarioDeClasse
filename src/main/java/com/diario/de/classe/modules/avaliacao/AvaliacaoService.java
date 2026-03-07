package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.calendario.CalendarioEscolarRepository;
import com.diario.de.classe.modules.turma.DisciplinaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {
    private final AvaliacaoRepository repository;
    private final DisciplinaRepository disciplinaRepository;
    private final CalendarioEscolarRepository calendarioEscolarRepository;

    public AvaliacaoService(AvaliacaoRepository repository, DisciplinaRepository disciplinaRepository,
                            CalendarioEscolarRepository calendarioEscolarRepository) {
        this.repository = repository;
        this.disciplinaRepository = disciplinaRepository;
        this.calendarioEscolarRepository = calendarioEscolarRepository;
    }

    public List<Avaliacao> buscarTodos() { return repository.findAll(); }

    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
    }

    public Avaliacao criar(Avaliacao avaliacao, Long idDisciplina, Long idCalendarioEscolar) {
        avaliacao.setDisciplina(disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id: " + idDisciplina)));
        if (idCalendarioEscolar != null) {
            avaliacao.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                    .orElseThrow(() -> new ResourceNotFoundException("Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        }
        return repository.save(avaliacao);
    }

    public Avaliacao atualizar(Long id, Avaliacao dados, Long idDisciplina, Long idCalendarioEscolar) {
        Avaliacao existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idAvaliacao", "createdAt", "disciplina", "calendarioEscolar");
        if (idDisciplina != null) existente.setDisciplina(disciplinaRepository.findById(idDisciplina)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id: " + idDisciplina)));
        if (idCalendarioEscolar != null) existente.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                .orElseThrow(() -> new ResourceNotFoundException("Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
