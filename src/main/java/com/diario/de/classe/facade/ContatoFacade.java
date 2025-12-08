package com.diario.de.classe.facade;

import com.diario.de.classe.dto.ContatoDTO;
import org.springframework.http.ResponseEntity;

public interface ContatoFacade {
    ResponseEntity<Object> buscarTodosContatos();

    ResponseEntity<Object> buscarContatoPoridContato(Long idContato);

    ResponseEntity<Object> criarContato(ContatoDTO contatoDTO);

    ResponseEntity<Object> atualizarContato(Long idContato, ContatoDTO contatoDTO);

    ResponseEntity<Object> deletarContato(Long idContato);

}
