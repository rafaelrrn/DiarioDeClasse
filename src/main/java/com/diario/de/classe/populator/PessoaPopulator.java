package com.diario.de.classe.populator;

import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.model.TipoPessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PessoaPopulator {
    final private static Logger LOG = LogManager.getLogger(PessoaPopulator.class);

    public Pessoa atualizaPessoa(Pessoa recebido, Pessoa doBanco) {
        if (recebido.getTipoPessoa() != null) doBanco.setTipoPessoa(recebido.getTipoPessoa());
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getSexo() != null) doBanco.setSexo(recebido.getSexo());
        if (recebido.getDataNascimento() != null) doBanco.setDataNascimento(recebido.getDataNascimento());
        if (recebido.getSituacao() != null) doBanco.setSituacao(recebido.getSituacao());
        if (recebido.getObs() != null) doBanco.setObs(recebido.getObs());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 