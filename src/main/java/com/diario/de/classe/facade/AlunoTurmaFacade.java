package com.diario.de.classe.facade;

import com.diario.de.classe.dto.AlunoTurmaDTO;
import org.springframework.http.ResponseEntity;

public interface AlunoTurmaFacade {
    ResponseEntity<Object> buscarTodosAlunosTurmas();

    ResponseEntity<Object> buscarAlunoTurmaPorIdAlunoTurma(Long idAlunoTurma);

    ResponseEntity<Object> criarAlunoTurma(AlunoTurmaDTO alunoTurmaDTO);

    ResponseEntity<Object> atualizarAlunoTurma(Long idAlunoTurma, AlunoTurmaDTO alunoTurmaDTO);

    ResponseEntity<Object> deletarAlunoTurma(Long idAlunoTurma);

}
