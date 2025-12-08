package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.TipoPessoaDTO;
import com.diario.de.classe.facade.TipoPessoaFacade;
import com.diario.de.classe.model.Serie;
import com.diario.de.classe.model.TipoPessoa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.TipoPessoaService;
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
public class TipoPessoaFacadeImpl implements TipoPessoaFacade {
    final private static Logger LOG = LogManager.getLogger(TipoPessoaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    TipoPessoaService tipoPessoaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosTipoPessoas() {
        List<TipoPessoa> entities = tipoPessoaService.buscarTodosTiposPessoas();
        List<TipoPessoaDTO> dtos = entities.stream().map(TipoPessoaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarTipoPessoaPoridTipoPessoa(Long idTipoPessoa) {
        TipoPessoa entity = tipoPessoaService.buscarTipoPessoaPoridTipoPessoa(idTipoPessoa);
        TipoPessoaDTO dto = entity != null ? new TipoPessoaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarTipoPessoa(TipoPessoaDTO tipoPessoaDTO) {
        TipoPessoa entity = new TipoPessoa();
        BeanUtils.copyProperties(tipoPessoaDTO, entity);
        TipoPessoa salvo = tipoPessoaService.criarTipoPessoa(entity);
        return defaultHandleResponse.responseHandler(new TipoPessoaDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarTipoPessoa(Long idTipoPessoa, TipoPessoaDTO tipoPessoaDTO) {
        TipoPessoa entity = tipoPessoaService.buscarTipoPessoaPoridTipoPessoa(idTipoPessoa);
        if(!ObjectUtils.isEmpty(entity)) {
            TipoPessoa atualizado = new TipoPessoa();
            BeanUtils.copyProperties(tipoPessoaDTO, atualizado);
            tipoPessoaService.atualizarTipoPessoa(idTipoPessoa, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new TipoPessoaDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarTipoPessoa(Long idTipoPessoa) {
        TipoPessoa tipoPessoa = tipoPessoaService.buscarTipoPessoaPoridTipoPessoa(idTipoPessoa);
        if(!ObjectUtils.isEmpty(tipoPessoa)) tipoPessoaService.deletarTipoPessoa(tipoPessoa);
        return defaultHandleResponse.responseHandler(tipoPessoa, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
