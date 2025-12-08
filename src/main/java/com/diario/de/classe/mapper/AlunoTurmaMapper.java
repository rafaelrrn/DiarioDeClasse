package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.AlunoTurmaDTO;
import com.diario.de.classe.model.AlunoTurma;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.service.TurmaService;
import org.springframework.stereotype.Component;

@Component
public class AlunoTurmaMapper {

    private final PessoaService pessoaService;

    private final TurmaService turmaService;

    public AlunoTurmaMapper(PessoaService pessoaService, TurmaService turmaService){
        this.pessoaService = pessoaService;
        this.turmaService = turmaService;
    }

    public AlunoTurma toEntity(AlunoTurmaDTO alunoTurmaDTO){
        if (alunoTurmaDTO == null){
            return null;
        }

        AlunoTurma entity = new AlunoTurma();
        entity.setIdAlunoTurma(alunoTurmaDTO.getIdAlunoTurma());

        if (alunoTurmaDTO.getIdAluno() != null){
            entity.setPessoaAluno(
                    pessoaService.buscarPessoaPoridPessoa(alunoTurmaDTO.getIdAluno())
            );
        }

        if (alunoTurmaDTO.getIdTurma() != null){
            entity.setTurma(
                    turmaService.buscarTurmaPoridTurma(alunoTurmaDTO.getIdTurma())
            );
        }

        entity.setObs(alunoTurmaDTO.getObs());

        return entity;
    }

    public AlunoTurmaDTO toDTO(AlunoTurma entity){
        if (entity == null){
            return null;
        }

        AlunoTurmaDTO alunoTurmaDTO = new AlunoTurmaDTO();
        alunoTurmaDTO.setIdAlunoTurma(entity.getIdAlunoTurma());

        alunoTurmaDTO.setIdAluno(
                entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null
        );

        alunoTurmaDTO.setIdTurma(
                entity.getTurma() != null ? entity.getTurma().getIdTurma() : null
        );

        alunoTurmaDTO.setObs(entity.getObs());
        alunoTurmaDTO.setCreatedAt(entity.getCreatedAt());
        alunoTurmaDTO.setUpdatedAt(entity.getUpdatedAt());

        return alunoTurmaDTO;
    }

    public void updateEntityFromDTO(AlunoTurmaDTO alunoTurmaDTO, AlunoTurma entity){
        if (alunoTurmaDTO == null || entity == null){
            return;
        }

        if (alunoTurmaDTO.getIdAluno() != null){
            entity.setPessoaAluno(
                    pessoaService.buscarPessoaPoridPessoa(alunoTurmaDTO.getIdAluno())
            );
        }

        if (alunoTurmaDTO.getIdTurma() != null){
            entity.setTurma(
                    turmaService.buscarTurmaPoridTurma(alunoTurmaDTO.getIdTurma())
            );
        }

        if (alunoTurmaDTO.getObs() != null){
            entity.setObs(alunoTurmaDTO.getObs());
        }
    }
}
