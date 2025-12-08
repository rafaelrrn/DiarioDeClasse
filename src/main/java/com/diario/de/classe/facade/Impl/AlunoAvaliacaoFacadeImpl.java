package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.AlunoAvaliacaoDTO;
import com.diario.de.classe.facade.AlunoAvaliacaoFacade;
import com.diario.de.classe.model.AlunoAvaliacao;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.AlunoAvaliacaoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.mapper.AlunoAvaliacaoMapper;
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
public class AlunoAvaliacaoFacadeImpl implements AlunoAvaliacaoFacade {
    final private static Logger LOG = LogManager.getLogger(AlunoAvaliacaoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    AlunoAvaliacaoService alunoAvaliacaoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;
    @Autowired
    private AlunoAvaliacaoMapper alunoAvaliacaoMapper;

    @Override
    public ResponseEntity<Object> buscarTodosAlunosAvaliacoes() {
        List<AlunoAvaliacao> entities = alunoAvaliacaoService.buscarTodosAlunosAvaliacoes();
        List<AlunoAvaliacaoDTO> dtos = entities.stream().map(AlunoAvaliacaoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarAlunoAvaliacaoPorIdAlunoAvaliacao(Long idAlunoAvaliacao) {
        AlunoAvaliacao entity = alunoAvaliacaoService.buscarAlunoAvaliacaoPorIdAlunoAvaliacao(idAlunoAvaliacao);
        AlunoAvaliacaoDTO dto = entity != null ? new AlunoAvaliacaoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarAlunoAvaliacao(AlunoAvaliacaoDTO alunoAvaliacaoDTO) {
        AlunoAvaliacao entity = alunoAvaliacaoMapper.toEntity(alunoAvaliacaoDTO);
        AlunoAvaliacao salvo = alunoAvaliacaoService.criarAlunoAvaliacao(entity);
        return defaultHandleResponse.responseHandler(alunoAvaliacaoMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarAlunoAvaliacao(Long idAlunoAvaliacao, AlunoAvaliacaoDTO alunoAvaliacaoDTO) {
        AlunoAvaliacao entity = alunoAvaliacaoService.buscarAlunoAvaliacaoPorIdAlunoAvaliacao(idAlunoAvaliacao);
        if(!ObjectUtils.isEmpty(entity)) {
            alunoAvaliacaoMapper.updateEntityFromDTO(alunoAvaliacaoDTO, entity);
            alunoAvaliacaoService.atualizarAlunoAvaliacao(entity);
        }
        return defaultHandleResponse.responseHandler(alunoAvaliacaoMapper.toDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarAlunoAvaliacao(Long idAlunoAvaliacao) {
        AlunoAvaliacao entity = alunoAvaliacaoService.buscarAlunoAvaliacaoPorIdAlunoAvaliacao(idAlunoAvaliacao);
        if(!ObjectUtils.isEmpty(entity)) alunoAvaliacaoService.deletarAlunoAvaliacao(entity);
        return defaultHandleResponse.responseHandler(new AlunoAvaliacaoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
