package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.ContatoPessoaDTO;
import com.diario.de.classe.model.ContatoPessoa;
import com.diario.de.classe.service.ContatoService;
import com.diario.de.classe.service.PessoaService;
import org.springframework.stereotype.Component;

@Component
public class ContatoPessoaMapper {

    private final PessoaService pessoaService;

    private final ContatoService contatoService;

    public ContatoPessoaMapper(PessoaService pessoaService, ContatoService contatoService){
        this.pessoaService = pessoaService;
        this.contatoService = contatoService;
    }

    public ContatoPessoa toEntity(ContatoPessoaDTO contatoPessoaDTO){
        if (contatoPessoaDTO == null){
            return null;
        }

        ContatoPessoa entity = new ContatoPessoa();
        entity.setIdContatoPessoa(contatoPessoaDTO.getIdContatoPessoa());

        if (contatoPessoaDTO.getIdPessoa() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(contatoPessoaDTO.getIdPessoa())
            );
        }

        if (contatoPessoaDTO.getIdContato() != null){
            entity.setContato(
                    contatoService.buscarContatoPoridContato(contatoPessoaDTO.getIdContato())
            );
        }

        entity.setNome(contatoPessoaDTO.getNome());

        return entity;
    }

    public ContatoPessoaDTO toDTO(ContatoPessoa entity){
        if (entity == null){
            return null;
        }

        ContatoPessoaDTO contatoPessoaDTO = new ContatoPessoaDTO();
        contatoPessoaDTO.setIdContatoPessoa(entity.getIdContatoPessoa());

        contatoPessoaDTO.setIdPessoa(
                entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null
        );

        contatoPessoaDTO.setIdContato(
                entity.getContato() != null ? entity.getContato().getIdContato() : null
        );

        contatoPessoaDTO.setNome(entity.getNome());
        contatoPessoaDTO.setCreatedAt(entity.getCreatedAt());
        contatoPessoaDTO.setUpdatedAt(entity.getUpdatedAt());

        return contatoPessoaDTO;
    }

    public void updateEntityFromDTO(ContatoPessoaDTO contatoPessoaDTO, ContatoPessoa entity){
        if (contatoPessoaDTO == null || entity == null){
            return;
        }

        if (contatoPessoaDTO.getIdPessoa() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(contatoPessoaDTO.getIdPessoa())
            );
        }

        if (contatoPessoaDTO.getIdContato() != null){
            entity.setContato(
                    contatoService.buscarContatoPoridContato(contatoPessoaDTO.getIdContato())
            );
        }

        if (contatoPessoaDTO.getNome() != null){
            entity.setNome(contatoPessoaDTO.getNome());
        }
    }
}
