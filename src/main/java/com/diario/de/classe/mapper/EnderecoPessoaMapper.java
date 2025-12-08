package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.EnderecoPessoaDTO;
import com.diario.de.classe.model.EnderecoPessoa;
import com.diario.de.classe.service.EnderecoService;
import com.diario.de.classe.service.PessoaService;
import org.springframework.stereotype.Component;

@Component
public class EnderecoPessoaMapper {

    private final PessoaService pessoaService;

    private final EnderecoService enderecoService;

    public EnderecoPessoaMapper(PessoaService pessoaService, EnderecoService enderecoService){
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
    }

    public EnderecoPessoa toEntity(EnderecoPessoaDTO enderecoPessoaDTO){
        if (enderecoPessoaDTO == null){
            return null;
        }

        EnderecoPessoa entity = new EnderecoPessoa();
        entity.setIdEnderecoPessoa(enderecoPessoaDTO.getIdEnderecoPessoa());

        if (enderecoPessoaDTO.getIdPessoa() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(enderecoPessoaDTO.getIdPessoa())
            );
        }

        if (enderecoPessoaDTO.getIdEndereco() != null){
            entity.setEndereco(
                    enderecoService.buscarEnderecoPoridEndereco(enderecoPessoaDTO.getIdEndereco())
            );
        }

        entity.setNome(enderecoPessoaDTO.getNome());

        return entity;
    }

    public EnderecoPessoaDTO toDTO(EnderecoPessoa entity){
        if (entity == null){
            return null;
        }

        EnderecoPessoaDTO enderecoPessoaDTO = new EnderecoPessoaDTO();
        enderecoPessoaDTO.setIdEnderecoPessoa(entity.getIdEnderecoPessoa());

        enderecoPessoaDTO.setIdPessoa(
                entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null
        );

        enderecoPessoaDTO.setIdEndereco(
                entity.getEndereco() != null ? entity.getEndereco().getIdEndereco() : null
        );

        enderecoPessoaDTO.setNome(entity.getNome());
        enderecoPessoaDTO.setCreatedAt(entity.getCreatedAt());
        enderecoPessoaDTO.setUpdatedAt(entity.getUpdatedAt());

        return enderecoPessoaDTO;
    }

    public void updateEntityFromDTO(EnderecoPessoaDTO enderecoPessoaDTO, EnderecoPessoa entity){
        if (enderecoPessoaDTO == null || entity == null){
            return;
        }

        if (enderecoPessoaDTO.getIdPessoa() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(enderecoPessoaDTO.getIdPessoa())
            );
        }

        if (enderecoPessoaDTO.getIdEndereco() != null){
            entity.setEndereco(
                    enderecoService.buscarEnderecoPoridEndereco(enderecoPessoaDTO.getIdEndereco())
            );
        }

        if (enderecoPessoaDTO.getNome() != null){
            entity.setNome(enderecoPessoaDTO.getNome());
        }
    }
}
