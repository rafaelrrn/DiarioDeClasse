package com.diario.de.classe.facade;

import com.diario.de.classe.dto.GrauDTO;
import org.springframework.http.ResponseEntity;

public interface GrauFacade {
    ResponseEntity<Object> buscarTodosGraus();

    ResponseEntity<Object> buscarGrauPoridGrau(Long idGrau);

    ResponseEntity<Object> criarGrau(GrauDTO grauDTO);

    ResponseEntity<Object> atualizarGrau(Long idGrau, GrauDTO grauDTO);

    ResponseEntity<Object> deletarGrau(Long idGrau);

}
