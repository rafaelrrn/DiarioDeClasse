package com.diario.de.classe.populator;

import com.diario.de.classe.model.ContatoPessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ContatoPessoaPopulator {
    final private static Logger LOG = LogManager.getLogger(ContatoPessoaPopulator.class);

    public ContatoPessoa atualizaContatoPessoa(ContatoPessoa contatoPessoaRecebido, ContatoPessoa contatoPessoaDoBanco) {
        if (contatoPessoaRecebido.getPessoa() != null) contatoPessoaDoBanco.setPessoa(contatoPessoaRecebido.getPessoa());
        if (contatoPessoaRecebido.getContato() != null) contatoPessoaDoBanco.setContato(contatoPessoaRecebido.getContato());
        if (contatoPessoaRecebido.getNome() != null) contatoPessoaDoBanco.setNome(contatoPessoaRecebido.getNome());
        if (contatoPessoaRecebido.getCreatedAt() != null) contatoPessoaDoBanco.setCreatedAt(contatoPessoaRecebido.getCreatedAt());
        if (contatoPessoaRecebido.getUpdatedAt() != null) contatoPessoaDoBanco.setUpdatedAt(contatoPessoaRecebido.getUpdatedAt());
        return contatoPessoaDoBanco;
    }
} 