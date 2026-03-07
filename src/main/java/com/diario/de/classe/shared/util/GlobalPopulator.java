package com.diario.de.classe.shared.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Utilitário genérico para atualização parcial de objetos.
 *
 * Itera os campos do objeto recebido e copia apenas os que não são nulos
 * para o objeto do banco, preservando os valores originais dos demais campos.
 * Usado pelos Services na operação de atualização (PUT/PATCH).
 */
@Component
public class GlobalPopulator {

    private static final Logger LOG = LogManager.getLogger(GlobalPopulator.class);

    /**
     * Atualiza o objeto do banco com os campos não nulos do novo objeto.
     *
     * @param objFromDb Objeto persistido a ser atualizado
     * @param newObj    Objeto com os novos valores recebidos da requisição
     * @param <T>       Tipo dos objetos (devem ser do mesmo tipo)
     * @return O objeto atualizado pronto para ser salvo
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
                LOG.error("Erro ao atualizar campo {}: {}", field.getName(), e.getMessage());
            }
        }

        return objFromDb;
    }
}
