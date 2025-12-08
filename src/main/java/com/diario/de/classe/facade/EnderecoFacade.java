package com.diario.de.classe.facade;

import com.diario.de.classe.dto.EnderecoDTO;
import org.springframework.http.ResponseEntity;

public interface EnderecoFacade {
    ResponseEntity<Object> buscarTodosEnderecos();

    ResponseEntity<Object> buscarEnderecoPoridEndereco(Long idEndereco);

    ResponseEntity<Object> criarEndereco(EnderecoDTO enderecoDTO);

    ResponseEntity<Object> atualizarEndereco(Long idEndereco, EnderecoDTO enderecoDTO);

    ResponseEntity<Object> deletarEndereco(Long idEndereco);

}
