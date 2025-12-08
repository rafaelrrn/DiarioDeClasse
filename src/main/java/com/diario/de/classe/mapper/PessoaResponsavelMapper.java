package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.PessoaResponsavelDTO;
import com.diario.de.classe.model.PessoaResponsavel;
import com.diario.de.classe.service.PessoaService;
import org.springframework.stereotype.Component;

@Component
public class PessoaResponsavelMapper {

    private final PessoaService pessoaService;

    public PessoaResponsavelMapper(PessoaService pessoaService){
        this.pessoaService = pessoaService;
    }

    public PessoaResponsavel toEntity(PessoaResponsavelDTO pessoaResponsavelDTO){
        if (pessoaResponsavelDTO == null){
            return null;
        }

        PessoaResponsavel entity = new PessoaResponsavel();
        entity.setIdPessoaResponsavel(pessoaResponsavelDTO.getIdPessoaResponsavel());

        if (pessoaResponsavelDTO.getIdAluno() != null){
            entity.setPessoaAluno(
                    pessoaService.buscarPessoaPoridPessoa(pessoaResponsavelDTO.getIdAluno())
            );
        }

        if (pessoaResponsavelDTO.getIdResponsavel() != null){
            entity.setPessoaResponsavel(
                    pessoaService.buscarPessoaPoridPessoa(pessoaResponsavelDTO.getIdResponsavel())
            );
        }

        entity.setParentesco(pessoaResponsavelDTO.getParentesco());
        entity.setCreatedAt(pessoaResponsavelDTO.getCreatedAt());
        entity.setUpdatedAt(pessoaResponsavelDTO.getUpdatedAt());

        return entity;
    }

    public PessoaResponsavelDTO toDTO(PessoaResponsavel entity){
        if (entity == null){
            return null;
        }

        PessoaResponsavelDTO pessoaResponsavelDTO = new PessoaResponsavelDTO();
        pessoaResponsavelDTO.setIdPessoaResponsavel(entity.getIdPessoaResponsavel());

        pessoaResponsavelDTO.setIdAluno(
                entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null
        );

        pessoaResponsavelDTO.setIdResponsavel(
                entity.getPessoaResponsavel() != null ? entity.getPessoaResponsavel().getIdPessoa() : null
        );

        pessoaResponsavelDTO.setParentesco(entity.getParentesco());
        pessoaResponsavelDTO.setCreatedAt(entity.getCreatedAt());
        pessoaResponsavelDTO.setUpdatedAt(entity.getUpdatedAt());

        return pessoaResponsavelDTO;
    }

    public void updateEntityFromDTO(PessoaResponsavelDTO pessoaResponsavelDTO, PessoaResponsavel entity){
        if (pessoaResponsavelDTO == null || entity == null){
            return;
        }

        if (pessoaResponsavelDTO.getIdAluno() != null){
            entity.setPessoaAluno(
                    pessoaService.buscarPessoaPoridPessoa(pessoaResponsavelDTO.getIdAluno())
            );
        }

        if (pessoaResponsavelDTO.getIdResponsavel() != null){
            entity.setPessoaResponsavel(
                    pessoaService.buscarPessoaPoridPessoa(pessoaResponsavelDTO.getIdResponsavel())
            );
        }

        entity.setParentesco(pessoaResponsavelDTO.getParentesco());
        entity.setUpdatedAt(pessoaResponsavelDTO.getUpdatedAt());
    }
}
