package com.diario.de.classe.facade;

import com.diario.de.classe.dto.InstituicaoEnsinoDTO;
import org.springframework.http.ResponseEntity;

public interface InstituicaoEnsinoFacade {
    ResponseEntity<Object> buscarTodasInstituicoesEnsino();

    ResponseEntity<Object> buscarInstituicaoEnsinoPoridInstituicaoEnsino(Long idInstituicaoEnsino);

    ResponseEntity<Object> criarInstituicaoEnsino(InstituicaoEnsinoDTO instituicaoEnsinoDTO);

    ResponseEntity<Object> atualizarInstituicaoEnsino(Long idInstituicaoEnsino, InstituicaoEnsinoDTO instituicaoEnsinoDTO);

    ResponseEntity<Object> deletarInstituicaoEnsino(Long idInstituicaoEnsino);

}
