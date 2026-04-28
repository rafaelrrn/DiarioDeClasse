package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.EventoCalendarioDTO;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoCalendarioService {

    private final EventoCalendarioRepository repository;
    private final CronogramaAnualRepository cronogramaAnualRepository;

    public EventoCalendarioService(EventoCalendarioRepository repository,
                                   CronogramaAnualRepository cronogramaAnualRepository) {
        this.repository = repository;
        this.cronogramaAnualRepository = cronogramaAnualRepository;
    }

    public List<EventoCalendarioDTO> buscarTodos() {
        return repository.findAll().stream().map(EventoCalendarioDTO::new).toList();
    }

    public EventoCalendarioDTO buscarPorId(Long id) {
        return new EventoCalendarioDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento de calendário não encontrado com id: " + id)));
    }

    public List<EventoCalendarioDTO> buscarPorCronograma(Long idCronograma) {
        return repository.findByCronogramaAnual_IdCronogramaAnualOrderByDataInicio(idCronograma)
                .stream().map(EventoCalendarioDTO::new).toList();
    }

    @Transactional
    public EventoCalendarioDTO criar(EventoCalendario dados, Long idCronograma) {
        CronogramaAnual cronograma = cronogramaAnualRepository.findByIdCronogramaAnualAndAtivoTrue(idCronograma)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cronograma anual não encontrado com id: " + idCronograma));
        dados.setCronogramaAnual(cronograma);
        return new EventoCalendarioDTO(repository.save(dados));
    }

    @Transactional
    public EventoCalendarioDTO atualizar(Long id, EventoCalendario dados) {
        EventoCalendario existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento de calendário não encontrado com id: " + id));

        existente.setTitulo(dados.getTitulo());
        existente.setDescricao(dados.getDescricao());
        existente.setDataInicio(dados.getDataInicio());
        existente.setDataFim(dados.getDataFim());
        existente.setTipoEvento(dados.getTipoEvento());
        existente.setEhLetivo(dados.getEhLetivo());
        existente.setCor(dados.getCor());

        return new EventoCalendarioDTO(repository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        EventoCalendario existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento de calendário não encontrado com id: " + id));
        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
