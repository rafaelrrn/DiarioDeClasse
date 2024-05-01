package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.ClasseEscolar;
import com.diario.de.classe.populator.ClasseEscolarPopulator;
import com.diario.de.classe.repository.jpa.ClasseEscolarRepositoryJpa;
import com.diario.de.classe.service.ClasseEscolarService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseEscolarServiceImpl implements ClasseEscolarService {
    final private static Logger LOG = LogManager.getLogger(ClasseEscolarServiceImpl.class);

    @Autowired
    ClasseEscolarRepositoryJpa classeEscolarRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    ClasseEscolarPopulator  classeEscolarPopulator;

    @Override
    public List<ClasseEscolar> buscarTodasClasses() {
        List<ClasseEscolar> classeList = classeEscolarRepositoryJpa.findAll();
        return classeList;
    }

    @Override
    public ClasseEscolar buscarClassePorCodCls(Long codCls) {
        Optional<ClasseEscolar> classeEscolar = classeEscolarRepositoryJpa.findById(codCls);
        if (classeEscolar.isPresent()) return classeEscolar.get();
        return null;
    }

    @Override
    public ClasseEscolar criarClasse(String classe) {
        ClasseEscolar classeEscolar = conversorObjetoEntidadeUtil.converterObjetoEmEntidade(classe, ClasseEscolar.class);
        if (!ObjectUtils.isEmpty(classeEscolar)) classeEscolarRepositoryJpa.save(classeEscolar);
        return classeEscolar;
    }

    @Override
    public ClasseEscolar atualizarClasse(Long codCls, String classeEscolarBody, ClasseEscolar classeEscolarDoBanco) {
        ClasseEscolar classeEscolarRecebida = conversorObjetoEntidadeUtil.converterObjetoEmEntidade(classeEscolarBody, ClasseEscolar.class);
        classeEscolarDoBanco = classeEscolarPopulator.atualizaClasseEscolar(classeEscolarRecebida, classeEscolarDoBanco); //TODO: trocar pelo GlobalPopulator
        return classeEscolarRepositoryJpa.save(classeEscolarDoBanco);
    }

    @Override
    public ClasseEscolar deletarClasse(ClasseEscolar classeEscolar) {
        classeEscolarRepositoryJpa.delete(classeEscolar);
        return classeEscolar;
    }
}
