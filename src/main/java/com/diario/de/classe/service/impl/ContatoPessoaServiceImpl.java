package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.ContatoPessoa;
import com.diario.de.classe.populator.ContatoPessoaPopulator;
import com.diario.de.classe.repository.jpa.ContatoPessoaRepositoryJpa;
import com.diario.de.classe.service.ContatoPessoaService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ContatoPessoaServiceImpl implements ContatoPessoaService {
    final private static Logger LOG = LogManager.getLogger(ContatoPessoaServiceImpl.class);

    @Autowired
    ContatoPessoaRepositoryJpa contatoPessoaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    ContatoPessoaPopulator contatoPessoaPopulator;

    @Override
    public List<ContatoPessoa> buscarTodasPessoasContatos() {
        List<ContatoPessoa> contatoPessoaList = contatoPessoaRepositoryJpa.findAll();
        return contatoPessoaList;
    }

    @Override
    public ContatoPessoa buscarContatoPessoaPoridContatoPessoa(Long idContatoPessoa) {
        Optional<ContatoPessoa> contatoPessoa = contatoPessoaRepositoryJpa.findById(idContatoPessoa);
        if (contatoPessoa.isPresent()) return contatoPessoa.get();
        return null;
    }

    @Override
    public ContatoPessoa criarContatoPessoa(ContatoPessoa contatoPessoa) {
        if (!ObjectUtils.isEmpty(contatoPessoa)) {
            return contatoPessoaRepositoryJpa.save(contatoPessoa);
        }
        return contatoPessoa;
    }

    @Override
    public ContatoPessoa atualizarContatoPessoa(ContatoPessoa contatoPessoaDoBanco) {
        if (!ObjectUtils.isEmpty(contatoPessoaDoBanco)) {
            return contatoPessoaRepositoryJpa.save(contatoPessoaDoBanco);
        }
        return contatoPessoaDoBanco;
    }

    @Override
    public ContatoPessoa deletarContatoPessoa(ContatoPessoa contatoPessoa) {
        contatoPessoaRepositoryJpa.delete(contatoPessoa);
        return contatoPessoa;
    }
} 