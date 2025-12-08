package com.diario.de.classe.facade;

import com.diario.de.classe.dto.PessoaDTO;
import org.springframework.http.ResponseEntity;

public interface PessoaFacade {
    ResponseEntity<Object> buscarTodasPessoas();

    ResponseEntity<Object> buscarPessoaPoridPessoa(Long idPessoa);

    ResponseEntity<Object> criarPessoa(PessoaDTO pessoaDTO);

    ResponseEntity<Object> atualizarPessoa(Long idPessoa, PessoaDTO pessoaDTO);

    ResponseEntity<Object> deletarPessoa(Long idPessoa);

}
