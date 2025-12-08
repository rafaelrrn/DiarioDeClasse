package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.TipoPessoa;
import com.diario.de.classe.populator.TipoPessoaPopulator;
import com.diario.de.classe.repository.jpa.TipoPessoaRepositoryJpa;
import com.diario.de.classe.service.TipoPessoaService;
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
public class TipoPessoaServiceImpl implements TipoPessoaService {
    final private static Logger LOG = LogManager.getLogger(TipoPessoaServiceImpl.class);

    @Autowired
    TipoPessoaRepositoryJpa tipoPessoaRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    TipoPessoaPopulator tipoPessoaPopulator;

    @Override
    public List<TipoPessoa> buscarTodosTiposPessoas() {
        return tipoPessoaRepositoryJpa.findAll();
    }

    @Override
    public TipoPessoa buscarTipoPessoaPoridTipoPessoa(Long idTipoPessoa) {
        Optional<TipoPessoa> tipoPessoa = tipoPessoaRepositoryJpa.findById(idTipoPessoa);
        return tipoPessoa.orElse(null);
    }

    @Override
    public TipoPessoa criarTipoPessoa(TipoPessoa tipoPessoa) {
        if (!ObjectUtils.isEmpty(tipoPessoa)) {
            return tipoPessoaRepositoryJpa.save(tipoPessoa);
        }
        return tipoPessoa;
    }

    @Override
    public TipoPessoa atualizarTipoPessoa(Long idTipoPessoa, TipoPessoa tipoPessoa, TipoPessoa tipoPessoaDoBanco) {
        BeanUtils.copyProperties(tipoPessoa, tipoPessoaDoBanco);
        return tipoPessoaRepositoryJpa.save(tipoPessoaDoBanco);
    }

    @Override
    public TipoPessoa deletarTipoPessoa(TipoPessoa tipoPessoa) {
        tipoPessoaRepositoryJpa.delete(tipoPessoa);
        return tipoPessoa;
    }
} 