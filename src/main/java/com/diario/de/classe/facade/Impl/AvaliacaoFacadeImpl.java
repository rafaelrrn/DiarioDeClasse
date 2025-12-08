package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.AvaliacaoDTO;
import com.diario.de.classe.facade.AvaliacaoFacade;
import com.diario.de.classe.model.Avaliacao;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.AvaliacaoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.mapper.AvaliacaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AvaliacaoFacadeImpl implements AvaliacaoFacade {

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    AvaliacaoService avaliacaoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;
    @Autowired
    private AvaliacaoMapper avaliacaoMapper;

    @Override
    public ResponseEntity<Object> buscarTodoasAvaliacoes() {
        List<Avaliacao> entities = avaliacaoService.buscarTodoasAvaliacoes();
        List<AvaliacaoDTO> dtos = entities.stream().map(AvaliacaoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarAvaliacaoPoridAvaliacao(Long idAvaliacao) {
        Avaliacao entity = avaliacaoService.buscarAvaliacaoPoridAvaliacao(idAvaliacao);
        AvaliacaoDTO dto = entity != null ? new AvaliacaoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarAvaliacao(AvaliacaoDTO avaliacaoDTO) {
        Avaliacao entity = avaliacaoMapper.toEntity(avaliacaoDTO);
        Avaliacao salvo = avaliacaoService.criarAvaliacao(entity);
        return defaultHandleResponse.responseHandler(avaliacaoMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarAvaliacao(Long idAvaliacao, AvaliacaoDTO avaliacaoDTO) {
        Avaliacao entity = avaliacaoService.buscarAvaliacaoPoridAvaliacao(idAvaliacao);
        if(!ObjectUtils.isEmpty(entity)) {
            avaliacaoMapper.updateEntityFromDTO(avaliacaoDTO, entity);
            avaliacaoService.atualizarAvaliacao(entity);
        }
        return defaultHandleResponse.responseHandler(avaliacaoMapper.toDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarAvaliacao(Long idAvaliacao) {
        Avaliacao entity = avaliacaoService.buscarAvaliacaoPoridAvaliacao(idAvaliacao);
        if(!ObjectUtils.isEmpty(entity)) avaliacaoService.deletarAvaliacao(entity);
        return defaultHandleResponse.responseHandler(new AvaliacaoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
