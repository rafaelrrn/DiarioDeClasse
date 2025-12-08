package com.diario.de.classe.populator;

import com.diario.de.classe.model.InstituicaoEnsino;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class InstituicaoEnsinoPopulator {
    final private static Logger LOG = LogManager.getLogger(InstituicaoEnsinoPopulator.class);

    public InstituicaoEnsino atualizaInstituicaoEnsino(InstituicaoEnsino recebido, InstituicaoEnsino doBanco) {
        if (recebido.getDescricao() != null) doBanco.setDescricao(recebido.getDescricao());
        if (recebido.getCodigoEstadual() != null) doBanco.setCodigoEstadual(recebido.getCodigoEstadual());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 