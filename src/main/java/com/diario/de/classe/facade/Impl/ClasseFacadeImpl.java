package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.ClasseDTO;
import com.diario.de.classe.facade.ClasseFacade;
import com.diario.de.classe.mapper.ClasseMapper;
import com.diario.de.classe.model.Classe;
import com.diario.de.classe.service.ClasseService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class ClasseFacadeImpl implements ClasseFacade {
    final private static Logger LOG = LogManager.getLogger(ClasseFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private ClasseMapper classeMapper;
    @Autowired
    private ClasseService classeService;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasClasses() {
        List<Classe> entities = classeService.buscarTodasClasses();
        List<ClasseDTO> dtos = entities.stream().map(ClasseDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarClassePoridClasse(Long idClasse) {
        Classe entity = classeService.buscarClassePoridClasse(idClasse);
        ClasseDTO dto = entity != null ? new ClasseDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarClasse(ClasseDTO classeDTO) {
        Classe entity = classeMapper.toEntity(classeDTO);
        Classe salvo = classeService.criarClasse(entity);
        return defaultHandleResponse.responseHandler(classeMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarClasse(Long idClasse, ClasseDTO classeDTO) {
        Classe classeDoBanco = classeService.buscarClassePoridClasse(idClasse);
        if(!ObjectUtils.isEmpty(classeDoBanco)) {
            classeMapper.updateEntityFromDTO(classeDTO, classeDoBanco);
            classeService.atualizarClasse(classeDoBanco);
        }
        return defaultHandleResponse.responseHandler(classeMapper.toDTO(classeDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarClasse(Long idClasse) {
        Classe classe = classeService.buscarClassePoridClasse(idClasse);
        if(!ObjectUtils.isEmpty(classe)) classeService.deletarClasse(classe);
        return defaultHandleResponse.responseHandler(classe, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
