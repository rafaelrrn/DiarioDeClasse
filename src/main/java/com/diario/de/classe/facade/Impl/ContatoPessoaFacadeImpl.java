package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ContatoPessoaFacade;
import com.diario.de.classe.mapper.ContatoPessoaMapper;
import com.diario.de.classe.model.ContatoPessoa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.ContatoPessoaService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.ContatoPessoaDTO;
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
public class ContatoPessoaFacadeImpl implements ContatoPessoaFacade {
    final private static Logger LOG = LogManager.getLogger(ContatoPessoaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private ContatoPessoaMapper contatoPessoaMapper;
    @Autowired
    private ContatoPessoaService contatoPessoaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasPessoasContatos() {
        List<ContatoPessoa> entities = contatoPessoaService.buscarTodasPessoasContatos();
        List<ContatoPessoaDTO> dtos = entities.stream().map(ContatoPessoaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarContatoPessoaPoridContatoPessoa(Long idContatoPessoa) {
        ContatoPessoa entity = contatoPessoaService.buscarContatoPessoaPoridContatoPessoa(idContatoPessoa);
        ContatoPessoaDTO dto = entity != null ? new ContatoPessoaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarContatoPessoa(ContatoPessoaDTO contatoPessoaDTO) {
        ContatoPessoa entity = contatoPessoaMapper.toEntity(contatoPessoaDTO);
        ContatoPessoa salvo = contatoPessoaService.criarContatoPessoa(entity);
        return defaultHandleResponse.responseHandler(contatoPessoaMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarContatoPessoa(Long idContatoPessoa, ContatoPessoaDTO contatoPessoaDTO) {
        ContatoPessoa contatoPessoaDoBanco = contatoPessoaService.buscarContatoPessoaPoridContatoPessoa(idContatoPessoa);
        if(!ObjectUtils.isEmpty(contatoPessoaDoBanco)) {
            contatoPessoaMapper.updateEntityFromDTO(contatoPessoaDTO, contatoPessoaDoBanco);
            contatoPessoaService.atualizarContatoPessoa(contatoPessoaDoBanco);
        }
        return defaultHandleResponse.responseHandler(contatoPessoaMapper.toDTO(contatoPessoaDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarContatoPessoa(Long idContatoPessoa) {
        ContatoPessoa contatoPessoa = contatoPessoaService.buscarContatoPessoaPoridContatoPessoa(idContatoPessoa);
        if(!ObjectUtils.isEmpty(contatoPessoa)) contatoPessoaService.deletarContatoPessoa(contatoPessoa);
        return defaultHandleResponse.responseHandler(contatoPessoa, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
