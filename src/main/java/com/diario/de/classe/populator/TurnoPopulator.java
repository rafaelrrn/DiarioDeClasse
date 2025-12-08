package com.diario.de.classe.populator;

import com.diario.de.classe.model.Turno;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TurnoPopulator {
    final private static Logger LOG = LogManager.getLogger(TurnoPopulator.class);

    public Turno atualizaTurno(Turno recebido, Turno doBanco) {
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 