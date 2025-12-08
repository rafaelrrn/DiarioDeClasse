package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Endereco;
import com.diario.de.classe.repository.jpa.EnderecoRepositoryJpa;
import com.diario.de.classe.service.EnderecoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoServiceImpl implements EnderecoService {
    final private static Logger LOG = LogManager.getLogger(EnderecoServiceImpl.class);

    @Autowired
    EnderecoRepositoryJpa enderecoRepositoryJpa;

    @Override
    public List<Endereco> buscarTodosEnderecos() {
        return enderecoRepositoryJpa.findAll();
    }

    @Override
    public Endereco buscarEnderecoPoridEndereco(Long idEndereco) {
        Optional<Endereco> endereco = enderecoRepositoryJpa.findById(idEndereco);
        return endereco.orElse(null);
    }

    @Override
    public Endereco criarEndereco(Endereco endereco) {
        if (!ObjectUtils.isEmpty(endereco)) {
            return enderecoRepositoryJpa.save(endereco);
        }
        return endereco;
    }

    @Override
    public Endereco atualizarEndereco(Long idEndereco, Endereco endereco, Endereco enderecoDoBanco) {
        BeanUtils.copyProperties(endereco, enderecoDoBanco);
        return enderecoRepositoryJpa.save(enderecoDoBanco);
    }

    @Override
    public Endereco deletarEndereco(Endereco endereco) {
        enderecoRepositoryJpa.delete(endereco);
        return endereco;
    }
} 