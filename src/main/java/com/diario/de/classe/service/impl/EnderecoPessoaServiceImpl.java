package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.EnderecoPessoa;
import com.diario.de.classe.populator.EnderecoPessoaPopulator;
import com.diario.de.classe.repository.jpa.EnderecoPessoaRepositoryJpa;
import com.diario.de.classe.service.EnderecoPessoaService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoPessoaServiceImpl implements EnderecoPessoaService {
    final private static Logger LOG = LogManager.getLogger(EnderecoPessoaServiceImpl.class);

    @Autowired
    EnderecoPessoaRepositoryJpa enderecoPessoaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    EnderecoPessoaPopulator enderecoPessoaPopulator;

    @Override
    public List<EnderecoPessoa> buscarTodosEnderecosPessoas() {
        return enderecoPessoaRepositoryJpa.findAll();
    }

    @Override
    public EnderecoPessoa buscarEnderecoPessoaPoridEnderecoPessoa(Long idEnderecoPessoa) {
        Optional<EnderecoPessoa> enderecoPessoa = enderecoPessoaRepositoryJpa.findById(idEnderecoPessoa);
        return enderecoPessoa.orElse(null);
    }

    @Override
    public EnderecoPessoa criarEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
        if (!ObjectUtils.isEmpty(enderecoPessoa)) {
            return enderecoPessoaRepositoryJpa.save(enderecoPessoa);
        }
        return enderecoPessoa;
    }

    @Override
    public EnderecoPessoa atualizarEnderecoPessoa(EnderecoPessoa enderecoPessoaDoBanco) {
        if (!ObjectUtils.isEmpty(enderecoPessoaDoBanco)) {
            return enderecoPessoaRepositoryJpa.save(enderecoPessoaDoBanco);
        }
        return enderecoPessoaDoBanco;
    }

    @Override
    public EnderecoPessoa deletarEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
        enderecoPessoaRepositoryJpa.delete(enderecoPessoa);
        return enderecoPessoa;
    }
} 