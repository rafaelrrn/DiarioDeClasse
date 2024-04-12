package com.diario.de.classe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConversorObjetoEntidadeUtil {
    final private static Logger LOG = LogManager.getLogger(ConversorObjetoEntidadeUtil.class);
    private final ObjectMapper objectMapper;

    public ConversorObjetoEntidadeUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Transactional
    public <T> T converterObjetoEmEntidade(String json, Class<T> entityClass) {
        try {
            T entity = objectMapper.readValue(json, entityClass);

            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex.getMessage());
            return null;
        }
    }
}
