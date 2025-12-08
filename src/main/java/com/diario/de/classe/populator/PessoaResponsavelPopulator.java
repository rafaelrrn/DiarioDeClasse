package com.diario.de.classe.populator;

import com.diario.de.classe.model.PessoaResponsavel;
import com.diario.de.classe.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PessoaResponsavelPopulator {
    final private static Logger LOG = LogManager.getLogger(PessoaResponsavelPopulator.class);

    public PessoaResponsavel atualizaPessoaResponsavel(PessoaResponsavel recebido, PessoaResponsavel doBanco) {
        if (recebido.getPessoaAluno() != null) doBanco.setPessoaAluno(recebido.getPessoaAluno());
        if (recebido.getPessoaResponsavel() != null) doBanco.setPessoaResponsavel(recebido.getPessoaResponsavel());
        if (recebido.getParentesco() != null) doBanco.setParentesco(recebido.getParentesco());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 