package com.diario.de.classe.facade;

import com.diario.de.classe.dto.ContatoPessoaDTO;
import org.springframework.http.ResponseEntity;

public interface ContatoPessoaFacade {
    ResponseEntity<Object> buscarTodasPessoasContatos();

    ResponseEntity<Object> buscarContatoPessoaPoridContatoPessoa(Long idContatoPessoa);

    ResponseEntity<Object> criarContatoPessoa(ContatoPessoaDTO contatoPessoaDTO);

    ResponseEntity<Object> atualizarContatoPessoa(Long idContatoPessoa, ContatoPessoaDTO contatoPessoaDTO);

    ResponseEntity<Object> deletarContatoPessoa(Long idContatoPessoa);

}
