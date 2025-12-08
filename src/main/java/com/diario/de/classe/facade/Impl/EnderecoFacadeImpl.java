package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.EnderecoFacade;
import com.diario.de.classe.model.Disciplina;
import com.diario.de.classe.model.Endereco;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.EnderecoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.EnderecoDTO;
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
public class EnderecoFacadeImpl implements EnderecoFacade {
    final private static Logger LOG = LogManager.getLogger(EnderecoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosEnderecos() {
        List<Endereco> entities = enderecoService.buscarTodosEnderecos();
        List<EnderecoDTO> dtos = entities.stream().map(EnderecoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarEnderecoPoridEndereco(Long idEndereco) {
        Endereco entity = enderecoService.buscarEnderecoPoridEndereco(idEndereco);
        EnderecoDTO dto = entity != null ? new EnderecoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarEndereco(EnderecoDTO enderecoDTO) {
        Endereco entity = new Endereco();
        BeanUtils.copyProperties(enderecoDTO, entity);
        Endereco salvo = enderecoService.criarEndereco(entity);
        return defaultHandleResponse.responseHandler(new EnderecoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoService.buscarEnderecoPoridEndereco(idEndereco);
        if(!ObjectUtils.isEmpty(entity)) {
            Endereco atualizado = new Endereco();
            BeanUtils.copyProperties(enderecoDTO, atualizado);
            enderecoService.atualizarEndereco(idEndereco, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new EnderecoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarEndereco(Long idEndereco) {
        Endereco endereco = enderecoService.buscarEnderecoPoridEndereco(idEndereco);
        if(!ObjectUtils.isEmpty(endereco)) enderecoService.deletarEndereco(endereco);
        return defaultHandleResponse.responseHandler(endereco, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
