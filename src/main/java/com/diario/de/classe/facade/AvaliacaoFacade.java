package com.diario.de.classe.facade;

import com.diario.de.classe.dto.AvaliacaoDTO;
import org.springframework.http.ResponseEntity;

public interface AvaliacaoFacade {
    ResponseEntity<Object> buscarTodoasAvaliacoes();

    ResponseEntity<Object> buscarAvaliacaoPoridAvaliacao(Long idAvaliacao);

    ResponseEntity<Object> criarAvaliacao(AvaliacaoDTO avaliacaoDTO);

    ResponseEntity<Object> atualizarAvaliacao(Long idAvaliacao, AvaliacaoDTO avaliacaoDTO);

    ResponseEntity<Object> deletarAvaliacao(Long idAvaliacao);

}
