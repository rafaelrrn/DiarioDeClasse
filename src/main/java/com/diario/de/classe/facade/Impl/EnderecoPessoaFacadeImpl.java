package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.EnderecoPessoaFacade;
import com.diario.de.classe.mapper.EnderecoPessoaMapper;
import com.diario.de.classe.model.EnderecoPessoa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.EnderecoPessoaService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.EnderecoPessoaDTO;
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
public class EnderecoPessoaFacadeImpl implements EnderecoPessoaFacade {
    final private static Logger LOG = LogManager.getLogger(EnderecoPessoaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private EnderecoPessoaMapper enderecoPessoaMapper;
    @Autowired
    private EnderecoPessoaService enderecoPessoaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosEnderecoPessoas() {
        List<EnderecoPessoa> entities = enderecoPessoaService.buscarTodosEnderecosPessoas();
        List<EnderecoPessoaDTO> dtos = entities.stream().map(EnderecoPessoaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarEnderecoPessoaPorIdEnderecoPessoa(Long idEnderecoPessoa) {
        EnderecoPessoa entity = enderecoPessoaService.buscarEnderecoPessoaPoridEnderecoPessoa(idEnderecoPessoa);
        EnderecoPessoaDTO dto = entity != null ? new EnderecoPessoaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarEnderecoPessoa(EnderecoPessoaDTO enderecoPessoaDTO) {
        EnderecoPessoa entity = enderecoPessoaMapper.toEntity(enderecoPessoaDTO);
        EnderecoPessoa salvo = enderecoPessoaService.criarEnderecoPessoa(entity);
        return defaultHandleResponse.responseHandler(enderecoPessoaMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarEnderecoPessoa(Long idEnderecoPessoa, EnderecoPessoaDTO enderecoPessoaDTO) {
        EnderecoPessoa enderecoPessoaDoBanco = enderecoPessoaService.buscarEnderecoPessoaPoridEnderecoPessoa(idEnderecoPessoa);
        if(!ObjectUtils.isEmpty(enderecoPessoaDoBanco)) {
            enderecoPessoaMapper.updateEntityFromDTO(enderecoPessoaDTO, enderecoPessoaDoBanco);
            enderecoPessoaService.atualizarEnderecoPessoa(enderecoPessoaDoBanco);
        }
        return defaultHandleResponse.responseHandler(enderecoPessoaMapper.toDTO(enderecoPessoaDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarEnderecoPessoa(Long idEnderecoPessoa) {
        EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPessoaPoridEnderecoPessoa(idEnderecoPessoa);
        if(!ObjectUtils.isEmpty(enderecoPessoa)) enderecoPessoaService.deletarEnderecoPessoa(enderecoPessoa);
        return defaultHandleResponse.responseHandler(enderecoPessoa, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
