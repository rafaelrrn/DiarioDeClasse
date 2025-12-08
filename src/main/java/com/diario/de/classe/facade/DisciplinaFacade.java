package com.diario.de.classe.facade;

import com.diario.de.classe.dto.DisciplinaDTO;
import org.springframework.http.ResponseEntity;

public interface DisciplinaFacade {
    ResponseEntity<Object> buscarTodasDisciplinas();

    ResponseEntity<Object> buscarDisciplinaPoridDisciplina(Long idDisciplina);

    ResponseEntity<Object> criarDisciplina(DisciplinaDTO disciplinaDTO);

    ResponseEntity<Object> atualizarDisciplina(Long idDisciplina, DisciplinaDTO disciplinaDTO);

    ResponseEntity<Object> deletarDisciplina(Long idDisciplina);

}
