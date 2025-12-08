package com.diario.de.classe.service;

import com.diario.de.classe.model.ComponenteCurricular;
import com.diario.de.classe.dto.ComponenteCurricularDTO;
import java.util.List;

public interface ComponenteCurricularService {

    List<ComponenteCurricular> buscarTodosComponentesCurriculares();

    ComponenteCurricular buscarComponenteCurricularPoridComponenteCurricular(Long idComponenteCurricular);

    ComponenteCurricular criarComponenteCurricular(ComponenteCurricular componenteCurricular);

    ComponenteCurricular atualizarComponenteCurricular(Long idComponenteCurricular, ComponenteCurricular componenteCurricular, ComponenteCurricular componenteCurricularDoBanco);

    ComponenteCurricular deletarComponenteCurricular(ComponenteCurricular componenteCurricular);

}
