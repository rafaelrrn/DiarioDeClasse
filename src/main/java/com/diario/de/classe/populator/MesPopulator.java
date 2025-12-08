package com.diario.de.classe.populator;

import com.diario.de.classe.model.Mes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MesPopulator {
    final private static Logger LOG = LogManager.getLogger(MesPopulator.class);

    public Mes atualizaMes(Mes recebido, Mes doBanco) {
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 