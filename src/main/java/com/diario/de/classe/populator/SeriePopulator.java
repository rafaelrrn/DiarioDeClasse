package com.diario.de.classe.populator;

import com.diario.de.classe.model.Serie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class SeriePopulator {
    final private static Logger LOG = LogManager.getLogger(SeriePopulator.class);

    public Serie atualizaSerie(Serie recebido, Serie doBanco) {
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 