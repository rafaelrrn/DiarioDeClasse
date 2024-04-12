package com.diario.de.classe.populator;

import com.diario.de.classe.model.ClasseEscolar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ClasseEscolarPopulator {
    final private static Logger LOG = LogManager.getLogger(ClasseEscolarPopulator.class);

    public ClasseEscolar atualizaClasseEscolar(ClasseEscolar classeEscolarRecebida, ClasseEscolar classeEscolarDoBanco){
        if(classeEscolarRecebida.getAnoCls()!=null) classeEscolarDoBanco.setAnoCls(classeEscolarRecebida.getAnoCls());
        if(classeEscolarRecebida.getTrnCls()!=null) classeEscolarDoBanco.setTrnCls(classeEscolarRecebida.getTrnCls());
        if(classeEscolarRecebida.getEsnCls()!=null) classeEscolarDoBanco.setEsnCls(classeEscolarRecebida.getEsnCls());
        if(classeEscolarRecebida.getSreCls()!=null) classeEscolarDoBanco.setSreCls(classeEscolarRecebida.getSreCls());
        if(classeEscolarRecebida.getTrmCls()!=null) classeEscolarDoBanco.setTrmCls(classeEscolarRecebida.getTrmCls());
        return classeEscolarDoBanco;
    }

}
