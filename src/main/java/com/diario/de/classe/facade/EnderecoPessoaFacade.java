package com.diario.de.classe.facade;

import com.diario.de.classe.dto.EnderecoPessoaDTO;
import org.springframework.http.ResponseEntity;

public interface EnderecoPessoaFacade {
    ResponseEntity<Object> buscarTodosEnderecoPessoas();

    ResponseEntity<Object> buscarEnderecoPessoaPorIdEnderecoPessoa(Long idEnderecoPessoa);

    ResponseEntity<Object> criarEnderecoPessoa(EnderecoPessoaDTO enderecoPessoaDTO);

    ResponseEntity<Object> atualizarEnderecoPessoa(Long idEnderecoPessoa, EnderecoPessoaDTO enderecoPessoaDTO);

    ResponseEntity<Object> deletarEnderecoPessoa(Long idEnderecoPessoa);

}
