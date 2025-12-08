package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.InstituicaoEnsinoDTO;
import com.diario.de.classe.facade.InstituicaoEnsinoFacade;
import com.diario.de.classe.model.Grau;
import com.diario.de.classe.model.InstituicaoEnsino;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.InstituicaoEnsinoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class InstituicaoEnsinoFacadeImpl implements InstituicaoEnsinoFacade {
    final private static Logger LOG = LogManager.getLogger(InstituicaoEnsinoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    InstituicaoEnsinoService instituicaoEnsinoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasInstituicoesEnsino() {
        List<InstituicaoEnsino> entities = instituicaoEnsinoService.buscarTodasInstituicoesEnsino();
        List<InstituicaoEnsinoDTO> dtos = entities.stream().map(InstituicaoEnsinoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarInstituicaoEnsinoPoridInstituicaoEnsino(Long idInstituicaoEnsino) {
        InstituicaoEnsino entity = instituicaoEnsinoService.buscarInstituicaoEnsinoPoridInstituicaoEnsino(idInstituicaoEnsino);
        InstituicaoEnsinoDTO dto = entity != null ? new InstituicaoEnsinoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarInstituicaoEnsino(InstituicaoEnsinoDTO instituicaoEnsinoDTO) {
        InstituicaoEnsino entity = new InstituicaoEnsino();
        BeanUtils.copyProperties(instituicaoEnsinoDTO, entity);
        InstituicaoEnsino salvo = instituicaoEnsinoService.criarInstituicaoEnsino(entity);
        return defaultHandleResponse.responseHandler(new InstituicaoEnsinoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarInstituicaoEnsino(Long idInstituicaoEnsino, InstituicaoEnsinoDTO instituicaoEnsinoDTO) {
        InstituicaoEnsino entity = instituicaoEnsinoService.buscarInstituicaoEnsinoPoridInstituicaoEnsino(idInstituicaoEnsino);
        if(!ObjectUtils.isEmpty(entity)) {
            InstituicaoEnsino atualizado = new InstituicaoEnsino();
            BeanUtils.copyProperties(instituicaoEnsinoDTO, atualizado);
            instituicaoEnsinoService.atualizarInstituicaoEnsino(idInstituicaoEnsino, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new InstituicaoEnsinoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarInstituicaoEnsino(Long idInstituicaoEnsino) {
        InstituicaoEnsino instituicaoEnsino = instituicaoEnsinoService.buscarInstituicaoEnsinoPoridInstituicaoEnsino(idInstituicaoEnsino);
        if(!ObjectUtils.isEmpty(instituicaoEnsino)) instituicaoEnsinoService.deletarInstituicaoEnsino(instituicaoEnsino);
        return defaultHandleResponse.responseHandler(instituicaoEnsino, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
