package com.diario.de.classe.populator;

import com.diario.de.classe.model.EnderecoPessoa;
import com.diario.de.classe.model.Endereco;
import com.diario.de.classe.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EnderecoPessoaPopulator {
    final private static Logger LOG = LogManager.getLogger(EnderecoPessoaPopulator.class);

    public EnderecoPessoa atualizaEnderecoPessoa(EnderecoPessoa recebido, EnderecoPessoa doBanco) {
        if (recebido.getPessoa() != null) doBanco.setPessoa(recebido.getPessoa());
        if (recebido.getEndereco() != null) doBanco.setEndereco(recebido.getEndereco());
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 