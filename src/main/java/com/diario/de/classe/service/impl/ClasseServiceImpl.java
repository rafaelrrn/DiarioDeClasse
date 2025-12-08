package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.Classe;
import com.diario.de.classe.repository.jpa.ClasseRepositoryJpa;
import com.diario.de.classe.service.ClasseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseServiceImpl implements ClasseService {
    final private static Logger LOG = LogManager.getLogger(ClasseServiceImpl.class);

    @Autowired
    private ClasseRepositoryJpa classeRepositoryJpa;

    @Override
    public List<Classe> buscarTodasClasses() {
        return classeRepositoryJpa.findAll();
    }

    @Override
    public Classe buscarClassePoridClasse(Long idClasse) {
        Optional<Classe> classe = classeRepositoryJpa.findById(idClasse);
        return classe.orElse(null);
    }

    @Override
    public Classe criarClasse(Classe classe) {
        if (!ObjectUtils.isEmpty(classe)) {
            return classeRepositoryJpa.save(classe);
        }
        return classe;
    }

    @Override
    public Classe atualizarClasse(Classe classeDoBanco) {
        if (!ObjectUtils.isEmpty(classeDoBanco)) {
            return classeRepositoryJpa.save(classeDoBanco);
        }
        return classeDoBanco;
    }

    @Override
    public Classe deletarClasse(Classe classe) {
        classeRepositoryJpa.delete(classe);
        return classe;
    }
} 