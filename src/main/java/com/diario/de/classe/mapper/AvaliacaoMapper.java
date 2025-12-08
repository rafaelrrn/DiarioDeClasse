package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.AvaliacaoDTO;
import com.diario.de.classe.model.Avaliacao;
import com.diario.de.classe.model.Disciplina;
import com.diario.de.classe.model.CalendarioEscolar;
import com.diario.de.classe.service.DisciplinaService;
import com.diario.de.classe.service.CalendarioEscolarService;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {
    private final DisciplinaService disciplinaService;
    private final CalendarioEscolarService calendarioEscolarService;

    public AvaliacaoMapper(DisciplinaService disciplinaService, CalendarioEscolarService calendarioEscolarService) {
        this.disciplinaService = disciplinaService;
        this.calendarioEscolarService = calendarioEscolarService;
    }

    public Avaliacao toEntity(AvaliacaoDTO dto) {
        if (dto == null) {
            return null;
        }
        Avaliacao entity = new Avaliacao();
        entity.setIdAvaliacao(dto.getIdAvaliacao());
        if (dto.getIdDisciplina() != null) {
            Disciplina disciplina = disciplinaService.buscarDisciplinaPoridDisciplina(dto.getIdDisciplina());
            entity.setDisciplina(disciplina);
        }
        if (dto.getIdCalendarioEscolar() != null) {
            CalendarioEscolar calendario = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(dto.getIdCalendarioEscolar());
            entity.setCalendarioEscolar(calendario);
        }
        entity.setMateria(dto.getMateria());
        entity.setDia(dto.getDia());
        return entity;
    }

    public AvaliacaoDTO toDTO(Avaliacao entity) {
        if (entity == null) {
            return null;
        }
        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setIdAvaliacao(entity.getIdAvaliacao());
        dto.setIdDisciplina(entity.getDisciplina() != null ? entity.getDisciplina().getIdDisciplina() : null);
        dto.setIdCalendarioEscolar(entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null);
        dto.setMateria(entity.getMateria());
        dto.setDia(entity.getDia());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDTO(AvaliacaoDTO dto, Avaliacao entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getIdDisciplina() != null) {
            Disciplina disciplina = disciplinaService.buscarDisciplinaPoridDisciplina(dto.getIdDisciplina());
            entity.setDisciplina(disciplina);
        }
        if (dto.getIdCalendarioEscolar() != null) {
            CalendarioEscolar calendario = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(dto.getIdCalendarioEscolar());
            entity.setCalendarioEscolar(calendario);
        }
        if (dto.getMateria() != null) {
            entity.setMateria(dto.getMateria());
        }
        if (dto.getDia() != null) {
            entity.setDia(dto.getDia());
        }
    }
}
