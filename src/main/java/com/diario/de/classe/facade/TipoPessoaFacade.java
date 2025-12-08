package com.diario.de.classe.facade;

import com.diario.de.classe.dto.TipoPessoaDTO;
import org.springframework.http.ResponseEntity;

public interface TipoPessoaFacade {
    ResponseEntity<Object> buscarTodosTipoPessoas();

    ResponseEntity<Object> buscarTipoPessoaPoridTipoPessoa(Long idTipoPessoa);

    ResponseEntity<Object> criarTipoPessoa(TipoPessoaDTO tipoPessoaDTO);

    ResponseEntity<Object> atualizarTipoPessoa(Long idTipoPessoa, TipoPessoaDTO tipoPessoaDTO);

    ResponseEntity<Object> deletarTipoPessoa(Long idTipoPessoa);

}
