package com.diario.de.classe.facade;

import com.diario.de.classe.dto.MesDTO;
import org.springframework.http.ResponseEntity;

public interface MesFacade {
    ResponseEntity<Object> buscarTodosMeses();

    ResponseEntity<Object> buscarMesPoridMes(Long idMes);

    ResponseEntity<Object> criarMes(MesDTO mesDTO);

    ResponseEntity<Object> atualizarMes(Long idMes, MesDTO mesDTO);

    ResponseEntity<Object> deletarMes(Long idMes);

}
