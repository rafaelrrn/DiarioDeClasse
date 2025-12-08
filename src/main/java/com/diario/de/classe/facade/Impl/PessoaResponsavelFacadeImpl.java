package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.PessoaResponsavelDTO;
import com.diario.de.classe.facade.PessoaResponsavelFacade;
import com.diario.de.classe.mapper.PessoaResponsavelMapper;
import com.diario.de.classe.model.PessoaResponsavel;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.PessoaResponsavelService;
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
public class PessoaResponsavelFacadeImpl implements PessoaResponsavelFacade {
    final private static Logger LOG = LogManager.getLogger(PessoaResponsavelFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private PessoaResponsavelMapper pessoaResponsavelMapper;
    @Autowired
    private PessoaResponsavelService pessoaResponsavelService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasPessoasResponsaveis() {
        List<PessoaResponsavel> entities = pessoaResponsavelService.buscarTodasPessoasResponsaveis();
        List<PessoaResponsavelDTO> dtos = entities.stream().map(PessoaResponsavelDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarPessoaResponsavelPoridPessoa(Long idPessoaResponsavel) {
        PessoaResponsavel entity = pessoaResponsavelService.buscarPessoaResponsavelPoridPessoaResponsavel(idPessoaResponsavel);
        PessoaResponsavelDTO dto = pessoaResponsavelMapper.toDTO(entity);
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarPessoaResponsavel(PessoaResponsavelDTO pessoaResponsavelDTO) {
        PessoaResponsavel entity = pessoaResponsavelMapper.toEntity(pessoaResponsavelDTO);
        PessoaResponsavel salvo = pessoaResponsavelService.criarPessoaResponsavel(entity);
        return defaultHandleResponse.responseHandler(pessoaResponsavelMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarPessoaResponsavel(Long idPessoaResponsavel, PessoaResponsavelDTO pessoaResponsavelDTO) {
        PessoaResponsavel pessoaResponsavelDoBanco = pessoaResponsavelService.buscarPessoaResponsavelPoridPessoaResponsavel(idPessoaResponsavel);
        if(!ObjectUtils.isEmpty(pessoaResponsavelDoBanco)) {
            pessoaResponsavelMapper.updateEntityFromDTO(pessoaResponsavelDTO, pessoaResponsavelDoBanco);
            pessoaResponsavelService.atualizarPessoaResponsavel(pessoaResponsavelDoBanco);
        }
        return defaultHandleResponse.responseHandler(pessoaResponsavelMapper.toDTO(pessoaResponsavelDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarPessoaResponsavel(Long idPessoaResponsavel) {
        PessoaResponsavel pessoaResponsavel = pessoaResponsavelService.buscarPessoaResponsavelPoridPessoaResponsavel(idPessoaResponsavel);
        if(!ObjectUtils.isEmpty(pessoaResponsavel)) pessoaResponsavelService.deletarPessoaResponsavel(pessoaResponsavel);
        return defaultHandleResponse.responseHandler(pessoaResponsavel, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
