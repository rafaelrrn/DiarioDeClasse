package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.PessoaDTO;
import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.model.TipoPessoa;
import com.diario.de.classe.service.TipoPessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {

    private final TipoPessoaService tipoPessoaService;

    @Autowired
    public PessoaMapper(TipoPessoaService tipoPessoaService) {
        this.tipoPessoaService = tipoPessoaService;
    }

    // DTO -> Entity
    public Pessoa toEntity(PessoaDTO pessoaDTO){
        if (pessoaDTO == null){
            return null;
        }
        Pessoa entity = new Pessoa();
        entity.setIdPessoa(pessoaDTO.getIdPessoa());

        if (pessoaDTO.getIdTipoPessoa() != null){
            entity.setTipoPessoa(
                tipoPessoaService.buscarTipoPessoaPoridTipoPessoa(pessoaDTO.getIdTipoPessoa())
            );
        }

        entity.setNome(pessoaDTO.getNome());
        entity.setSexo(pessoaDTO.getSexo());
        entity.setDataNascimento(pessoaDTO.getDataNascimento());
        entity.setSituacao(pessoaDTO.getSituacao());
        entity.setObs(pessoaDTO.getObs());

        return entity;
    }

    //Entity -> DTO
    public PessoaDTO toDTO(Pessoa entity) {
        if (entity == null) {
            return null;
        }

        PessoaDTO pessoaDTO = new PessoaDTO();

        pessoaDTO.setIdPessoa(entity.getIdPessoa());
        pessoaDTO.setIdTipoPessoa(
                entity.getIdPessoa() != null ? entity.getTipoPessoa().getIdTipoPessoa() : null
        );
        pessoaDTO.setNome(entity.getNome());
        pessoaDTO.setSexo(entity.getSexo());
        pessoaDTO.setDataNascimento(entity.getDataNascimento());
        pessoaDTO.setSituacao(entity.getSituacao());
        pessoaDTO.setObs(entity.getObs());

        return pessoaDTO;
    }

    // Atualiza um entity existente com base no DTO
    public void updateEntityFromDTO(PessoaDTO pessoaDTO, Pessoa entity) {
        if (pessoaDTO == null || entity == null) {
            return;
        }

        if (pessoaDTO.getIdTipoPessoa() != null) {
            entity.setTipoPessoa(
                    tipoPessoaService.buscarTipoPessoaPoridTipoPessoa(pessoaDTO.getIdTipoPessoa())
            );
        }

        entity.setNome(pessoaDTO.getNome());
        entity.setSexo(pessoaDTO.getSexo());
        entity.setDataNascimento(pessoaDTO.getDataNascimento());
        entity.setSituacao(pessoaDTO.getSituacao());
        entity.setObs(pessoaDTO.getObs());
    }

}
