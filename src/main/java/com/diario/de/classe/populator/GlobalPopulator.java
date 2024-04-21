package com.diario.de.classe.populator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class GlobalPopulator {
    final private static Logger LOG = LogManager.getLogger(GlobalPopulator.class);

    /**
     * Atualiza o objeto fornecido com os campos não nulos de outro objeto.
     *
     * @param objFromDb O objeto a ser atualizado.
     * @param newObj    O objeto que contém os novos valores.
     * @param <T>       O tipo dos objetos.
     * @return O objeto atualizado.
     */
    public <T> T updateGenericObj(T objFromDb, T newObj) {
        Class<?> objClass = objFromDb.getClass();

        Field[] fields = objClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(newObj);
                if (fieldValue != null) field.set(objFromDb, fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }

        return objFromDb;
    }
}
