package com.diario.de.classe.populator;

import com.diario.de.classe.model.Endereco;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EnderecoPopulator {
    final private static Logger LOG = LogManager.getLogger(EnderecoPopulator.class);

    public Endereco atualizaEndereco(Endereco enderecoRecebido, Endereco enderecoDoBanco) {
        if (enderecoRecebido.getUf() != null) enderecoDoBanco.setUf(enderecoRecebido.getUf());
        if (enderecoRecebido.getCidade() != null) enderecoDoBanco.setCidade(enderecoRecebido.getCidade());
        if (enderecoRecebido.getBairro() != null) enderecoDoBanco.setBairro(enderecoRecebido.getBairro());
        if (enderecoRecebido.getRua() != null) enderecoDoBanco.setRua(enderecoRecebido.getRua());
        if (enderecoRecebido.getNumero() != null) enderecoDoBanco.setNumero(enderecoRecebido.getNumero());
        if (enderecoRecebido.getCep() != null) enderecoDoBanco.setCep(enderecoRecebido.getCep());
        if (enderecoRecebido.getComplemento() != null) enderecoDoBanco.setComplemento(enderecoRecebido.getComplemento());
        if (enderecoRecebido.getCreatedAt() != null) enderecoDoBanco.setCreatedAt(enderecoRecebido.getCreatedAt());
        if (enderecoRecebido.getUpdatedAt() != null) enderecoDoBanco.setUpdatedAt(enderecoRecebido.getUpdatedAt());
        return enderecoDoBanco;
    }
} 