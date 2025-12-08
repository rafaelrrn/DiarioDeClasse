package com.diario.de.classe.facade;

import com.diario.de.classe.dto.AlunoAvaliacaoDTO;
import org.springframework.http.ResponseEntity;

public interface AlunoAvaliacaoFacade {
    ResponseEntity<Object> buscarTodosAlunosAvaliacoes();

    ResponseEntity<Object> buscarAlunoAvaliacaoPorIdAlunoAvaliacao(Long idAlunoAvaliacao);

    ResponseEntity<Object> criarAlunoAvaliacao(AlunoAvaliacaoDTO alunoAvaliacaoDTO);

    ResponseEntity<Object> atualizarAlunoAvaliacao(Long idAlunoAvaliacao, AlunoAvaliacaoDTO alunoAvaliacaoDTO);

    ResponseEntity<Object> deletarAlunoAvaliacao(Long idAlunoAvaliacao);

}
