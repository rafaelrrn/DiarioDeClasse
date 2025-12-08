package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.model.AlunoAvaliacao;
import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.model.Avaliacao;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.service.AvaliacaoService;
import org.springframework.stereotype.Component;

@Component
public class AlunoAvaliacaoMapper {
    private final PessoaService pessoaService;
    private final AvaliacaoService avaliacaoService;

    public AlunoAvaliacaoMapper(PessoaService pessoaService, AvaliacaoService avaliacaoService) {
        this.pessoaService = pessoaService;
        this.avaliacaoService = avaliacaoService;
    }

    public AlunoAvaliacao toEntity(AlunoAvaliacaoDTO dto) {
        if (dto == null) {
            return null;
        }
        AlunoAvaliacao entity = new AlunoAvaliacao();
        entity.setIdAlunoAvaliacao(dto.getIdAlunoAvaliacao());
        if (dto.getIdAluno() != null) {
            Pessoa aluno = pessoaService.buscarPessoaPoridPessoa(dto.getIdAluno());
            entity.setPessoaAluno(aluno);
        }
        if (dto.getIdAvaliacao() != null) {
            Avaliacao avaliacao = avaliacaoService.buscarAvaliacaoPoridAvaliacao(dto.getIdAvaliacao());
            entity.setAvaliacao(avaliacao);
        }
        entity.setNota(dto.getNota());
        entity.setObs(dto.getObs());
        return entity;
    }

    public AlunoAvaliacaoDTO toDTO(AlunoAvaliacao entity) {
        if (entity == null) {
            return null;
        }
        AlunoAvaliacaoDTO dto = new AlunoAvaliacaoDTO();
        dto.setIdAlunoAvaliacao(entity.getIdAlunoAvaliacao());
        dto.setIdAluno(entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null);
        dto.setIdAvaliacao(entity.getAvaliacao() != null ? entity.getAvaliacao().getIdAvaliacao() : null);
        dto.setNota(entity.getNota());
        dto.setObs(entity.getObs());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public void updateEntityFromDTO(AlunoAvaliacaoDTO dto, AlunoAvaliacao entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getIdAluno() != null) {
            Pessoa aluno = pessoaService.buscarPessoaPoridPessoa(dto.getIdAluno());
            entity.setPessoaAluno(aluno);
        }
        if (dto.getIdAvaliacao() != null) {
            Avaliacao avaliacao = avaliacaoService.buscarAvaliacaoPoridAvaliacao(dto.getIdAvaliacao());
            entity.setAvaliacao(avaliacao);
        }
        if (dto.getNota() != null) {
            entity.setNota(dto.getNota());
        }
        if (dto.getObs() != null) {
            entity.setObs(dto.getObs());
        }
    }
}
