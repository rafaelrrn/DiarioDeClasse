package com.diario.de.classe.service.impl;


import com.diario.de.classe.model.ComponenteCurricular;
import com.diario.de.classe.repository.jpa.ComponenteCurricularRepositoryJpa;
import com.diario.de.classe.service.ComponenteCurricularService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ComponenteCurricularServiceImpl implements ComponenteCurricularService {
    final private static Logger LOG = LogManager.getLogger(ComponenteCurricularServiceImpl.class);

    @Autowired
    private ComponenteCurricularRepositoryJpa componenteCurricularRepositoryJpa;

    @Override
    public List<ComponenteCurricular> buscarTodosComponentesCurriculares() {
        return componenteCurricularRepositoryJpa.findAll();
    }

    @Override
    public ComponenteCurricular buscarComponenteCurricularPoridComponenteCurricular(Long idComponenteCurricular) {
        Optional<ComponenteCurricular> componente = componenteCurricularRepositoryJpa.findById(idComponenteCurricular);
        return componente.orElse(null);
    }

    @Override
    public ComponenteCurricular criarComponenteCurricular(ComponenteCurricular componenteCurricular) {
        if (!ObjectUtils.isEmpty(componenteCurricular)) {
            return componenteCurricularRepositoryJpa.save(componenteCurricular);
        }
        return componenteCurricular;
    }

    @Override
    public ComponenteCurricular atualizarComponenteCurricular(Long idComponenteCurricular, ComponenteCurricular componenteCurricular, ComponenteCurricular componenteCurricularDoBanco) {
        BeanUtils.copyProperties(componenteCurricular, componenteCurricularDoBanco);
        return componenteCurricularRepositoryJpa.save(componenteCurricularDoBanco);
    }

    @Override
    public ComponenteCurricular deletarComponenteCurricular(ComponenteCurricular componenteCurricular) {
        componenteCurricularRepositoryJpa.delete(componenteCurricular);
        return componenteCurricular;
    }
} 