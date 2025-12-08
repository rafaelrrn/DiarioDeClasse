package com.diario.de.classe.populator;

import com.diario.de.classe.model.Periodo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PeriodoPopulator {
    final private static Logger LOG = LogManager.getLogger(PeriodoPopulator.class);

    public Periodo atualizaPeriodo(Periodo recebido, Periodo doBanco) {
        if (recebido.getNome() != null) doBanco.setNome(recebido.getNome());
        if (recebido.getCreatedAt() != null) doBanco.setCreatedAt(recebido.getCreatedAt());
        if (recebido.getUpdatedAt() != null) doBanco.setUpdatedAt(recebido.getUpdatedAt());
        return doBanco;
    }
} 