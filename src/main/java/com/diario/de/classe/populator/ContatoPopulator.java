package com.diario.de.classe.populator;

import com.diario.de.classe.model.Contato;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ContatoPopulator {
    final private static Logger LOG = LogManager.getLogger(ContatoPopulator.class);

    public Contato atualizaContato(Contato contatoRecebido, Contato contatoDoBanco) {
        if (contatoRecebido.getTipoContato() != null) contatoDoBanco.setTipoContato(contatoRecebido.getTipoContato());
        if (contatoRecebido.getContato() != null) contatoDoBanco.setContato(contatoRecebido.getContato());
        if (contatoRecebido.getCreatedAt() != null) contatoDoBanco.setCreatedAt(contatoRecebido.getCreatedAt());
        if (contatoRecebido.getUpdatedAt() != null) contatoDoBanco.setUpdatedAt(contatoRecebido.getUpdatedAt());
        return contatoDoBanco;
    }
} 