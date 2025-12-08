package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Contato;
import com.diario.de.classe.populator.ContatoPopulator;
import com.diario.de.classe.repository.jpa.ContatoRepositoryJpa;
import com.diario.de.classe.service.ContatoService;
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
public class ContatoServiceImpl implements ContatoService {
    final private static Logger LOG = LogManager.getLogger(ContatoServiceImpl.class);

    @Autowired
    ContatoRepositoryJpa contatoRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    ContatoPopulator contatoPopulator;

    @Override
    public List<Contato> buscarTodosContatos() {
        List<Contato> contatoList = contatoRepositoryJpa.findAll();
        return contatoList;
    }

    @Override
    public Contato buscarContatoPoridContato(Long idContato) {
        Optional<Contato> contato = contatoRepositoryJpa.findById(idContato);
        if (contato.isPresent()) return contato.get();
        return null;
    }

    @Override
    public Contato criarContato(Contato contato) {
        if (!ObjectUtils.isEmpty(contato)) {
            return contatoRepositoryJpa.save(contato);
        }
        return contato;
    }

    @Override
    public Contato atualizarContato(Long idContato, Contato contato, Contato contatoDoBanco) {
        BeanUtils.copyProperties(contato, contatoDoBanco);
        return contatoRepositoryJpa.save(contatoDoBanco);
    }

    @Override
    public Contato deletarContato(Contato contato) {
        contatoRepositoryJpa.delete(contato);
        return contato;
    }
} 