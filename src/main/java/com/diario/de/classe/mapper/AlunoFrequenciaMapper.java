package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.AlunoFrequenciaDTO;
import com.diario.de.classe.model.AlunoFrequencia;
import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.model.CalendarioEscolar;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.service.CalendarioEscolarService;
import org.springframework.stereotype.Component;

@Component
public class AlunoFrequenciaMapper {

    private final PessoaService pessoaService;
    private final CalendarioEscolarService calendarioEscolarService;

    public AlunoFrequenciaMapper(PessoaService pessoaService, CalendarioEscolarService calendarioEscolarService) {
        this.pessoaService = pessoaService;
        this.calendarioEscolarService = calendarioEscolarService;
    }

    public AlunoFrequencia toEntity(AlunoFrequenciaDTO dto) {
        if (dto == null) {
            return null;
        }
        AlunoFrequencia entity = new AlunoFrequencia();
        entity.setIdAlunoFrequencia(dto.getIdAlunoFrequencia());
        if (dto.getIdAluno() != null) {
            Pessoa aluno = pessoaService.buscarPessoaPoridPessoa(dto.getIdAluno());
            entity.setPessoaAluno(aluno);
        }
        if (dto.getIdCalendarioEscolar() != null) {
            CalendarioEscolar calendario = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(dto.getIdCalendarioEscolar());
            entity.setCalendarioEscolar(calendario);
        }
        entity.setFaltas(dto.getFaltas());
        return entity;
    }

    public AlunoFrequenciaDTO toDTO(AlunoFrequencia entity) {
        if (entity == null) {
            return null;
        }
        AlunoFrequenciaDTO dto = new AlunoFrequenciaDTO();
        dto.setIdAlunoFrequencia(entity.getIdAlunoFrequencia());
        dto.setIdAluno(entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null);
        dto.setIdCalendarioEscolar(entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null);
        dto.setFaltas(entity.getFaltas());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDTO(AlunoFrequenciaDTO dto, AlunoFrequencia entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getIdAluno() != null) {
            Pessoa aluno = pessoaService.buscarPessoaPoridPessoa(dto.getIdAluno());
            entity.setPessoaAluno(aluno);
        }
        if (dto.getIdCalendarioEscolar() != null) {
            CalendarioEscolar calendario = calendarioEscolarService.buscarCalendarioEscolarPoridCalendarioEscolar(dto.getIdCalendarioEscolar());
            entity.setCalendarioEscolar(calendario);
        }
        if (dto.getFaltas() != null) {
            entity.setFaltas(dto.getFaltas());
        }
    }
}
