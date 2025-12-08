package com.diario.de.classe.facade;

import com.diario.de.classe.dto.PessoaResponsavelDTO;
import org.springframework.http.ResponseEntity;

public interface PessoaResponsavelFacade {
    ResponseEntity<Object> buscarTodasPessoasResponsaveis();

    ResponseEntity<Object> buscarPessoaResponsavelPoridPessoa(Long idPessoaResponsavel);

    ResponseEntity<Object> criarPessoaResponsavel(PessoaResponsavelDTO pessoaResponsavelDTO);

    ResponseEntity<Object> atualizarPessoaResponsavel(Long idPessoaResponsavel, PessoaResponsavelDTO pessoaResponsavelDTO);

    ResponseEntity<Object> deletarPessoaResponsavel(Long idPessoaResponsavel);

}
