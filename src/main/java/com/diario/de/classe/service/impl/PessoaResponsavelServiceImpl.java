package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.PessoaResponsavel;
import com.diario.de.classe.populator.PessoaResponsavelPopulator;
import com.diario.de.classe.repository.jpa.PessoaResponsavelRepositoryJpa;
import com.diario.de.classe.service.PessoaResponsavelService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaResponsavelServiceImpl implements PessoaResponsavelService {
    final private static Logger LOG = LogManager.getLogger(PessoaResponsavelServiceImpl.class);

    @Autowired
    PessoaResponsavelRepositoryJpa pessoaResponsavelRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    PessoaResponsavelPopulator pessoaResponsavelPopulator;

    @Override
    public List<PessoaResponsavel> buscarTodasPessoasResponsaveis() {
        return pessoaResponsavelRepositoryJpa.findAll();
    }

    @Override
    public PessoaResponsavel buscarPessoaResponsavelPoridPessoaResponsavel(Long idPessoaResponsavel) {
        Optional<PessoaResponsavel> pessoaResponsavel = pessoaResponsavelRepositoryJpa.findById(idPessoaResponsavel);
        return pessoaResponsavel.orElse(null);
    }

    @Override
    public PessoaResponsavel criarPessoaResponsavel(PessoaResponsavel pessoaResponsavel) {
        if (!ObjectUtils.isEmpty(pessoaResponsavel)) {
            return pessoaResponsavelRepositoryJpa.save(pessoaResponsavel);
        }
        return pessoaResponsavel;
    }

    @Override
    public PessoaResponsavel atualizarPessoaResponsavel(PessoaResponsavel pessoaResponsavelDoBanco) {
        if (!ObjectUtils.isEmpty(pessoaResponsavelDoBanco)) {
            return pessoaResponsavelRepositoryJpa.save(pessoaResponsavelDoBanco);
        }
        return pessoaResponsavelDoBanco;
    }

    @Override
    public PessoaResponsavel deletarPessoaResponsavel(PessoaResponsavel pessoaResponsavel) {
        pessoaResponsavelRepositoryJpa.delete(pessoaResponsavel);
        return pessoaResponsavel;
    }
} 