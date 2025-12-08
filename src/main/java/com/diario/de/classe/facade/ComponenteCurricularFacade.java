package com.diario.de.classe.facade;

import com.diario.de.classe.dto.ComponenteCurricularDTO;
import org.springframework.http.ResponseEntity;

public interface ComponenteCurricularFacade {
    ResponseEntity<Object> buscarTodosComponentesCurriculares();

    ResponseEntity<Object> buscarComponenteCurricularPoridComponenteCurricular(Long idComponenteCurricular);

    ResponseEntity<Object> criarComponenteCurricular(ComponenteCurricularDTO componenteCurricularDTO);

    ResponseEntity<Object> atualizarComponenteCurricular(Long idComponenteCurricular, ComponenteCurricularDTO componenteCurricularDTO);

    ResponseEntity<Object> deletarComponenteCurricular(Long idComponenteCurricular);

}
