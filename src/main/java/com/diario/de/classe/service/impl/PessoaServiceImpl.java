package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.populator.PessoaPopulator;
import com.diario.de.classe.repository.jpa.PessoaRepositoryJpa;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {
    final private static Logger LOG = LogManager.getLogger(PessoaServiceImpl.class);

    @Autowired
    PessoaRepositoryJpa pessoaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    PessoaPopulator pessoaPopulator;

    @Override
    public List<Pessoa> buscarTodasPessoas() {
        return pessoaRepositoryJpa.findAll();
    }

    @Override
    public Pessoa buscarPessoaPoridPessoa(Long idPessoa) {
        Optional<Pessoa> pessoa = pessoaRepositoryJpa.findById(idPessoa);
        return pessoa.orElse(null);
    }

    @Override
    public Pessoa criarPessoa(Pessoa pessoa) {
        if (!ObjectUtils.isEmpty(pessoa)) {
            return pessoaRepositoryJpa.save(pessoa);
        }
        return pessoa;
    }

    @Override
    public Pessoa atualizarPessoa(Pessoa pessoaDoBanco) {
        if (!ObjectUtils.isEmpty(pessoaDoBanco)) {
            return pessoaRepositoryJpa.save(pessoaDoBanco);
        }
        return pessoaDoBanco;
    }

    @Override
    public Pessoa deletarPessoa(Pessoa pessoa) {
        pessoaRepositoryJpa.delete(pessoa);
        return pessoa;
    }
} 