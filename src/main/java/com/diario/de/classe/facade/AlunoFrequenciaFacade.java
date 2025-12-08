package com.diario.de.classe.facade;

import com.diario.de.classe.dto.AlunoFrequenciaDTO;
import org.springframework.http.ResponseEntity;

public interface AlunoFrequenciaFacade {
    ResponseEntity<Object> buscarTodosAlunosFrequencias();

    ResponseEntity<Object> buscarAlunoFrequenciaPorIdAlunoFrequencia(Long IdAlunoFrequencia);

    ResponseEntity<Object> criarAlunoFrequencia(AlunoFrequenciaDTO alunoFrequenciaDTO);

    ResponseEntity<Object> atualizarAlunoFrequencia(Long IdAlunoFrequencia, AlunoFrequenciaDTO alunoFrequenciaDTO);

    ResponseEntity<Object> deletarAlunoFrequencia(Long IdAlunoFrequencia);

}
