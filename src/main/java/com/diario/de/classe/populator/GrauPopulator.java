package com.diario.de.classe.populator;

import com.diario.de.classe.model.Grau;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GrauPopulator {
    final private static Logger LOG = LogManager.getLogger(GrauPopulator.class);

    public Grau atualizaGrau(Grau recebido, Grau doBanco) {
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 