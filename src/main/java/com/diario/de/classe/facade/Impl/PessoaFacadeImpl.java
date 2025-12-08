package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.PessoaDTO;
import com.diario.de.classe.facade.PessoaFacade;
import com.diario.de.classe.mapper.PessoaMapper;
import com.diario.de.classe.model.Pessoa;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.PessoaService;
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
public class PessoaFacadeImpl implements PessoaFacade {
    final private static Logger LOG = LogManager.getLogger(PessoaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private PessoaMapper pessoaMapper;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasPessoas() {
        List<Pessoa> entities = pessoaService.buscarTodasPessoas();
        List<PessoaDTO> dtos = entities.stream().map(PessoaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarPessoaPoridPessoa(Long idPessoa) {
        Pessoa entity = pessoaService.buscarPessoaPoridPessoa(idPessoa);
        PessoaDTO dto = entity != null ? pessoaMapper.toDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarPessoa(PessoaDTO pessoaDTO) {
        Pessoa entity = pessoaMapper.toEntity(pessoaDTO);
        Pessoa salvo = pessoaService.criarPessoa(entity);
        return defaultHandleResponse.responseHandler(pessoaMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarPessoa(Long idPessoa, PessoaDTO pessoaDTO) {
        Pessoa pessoaDoBanco = pessoaService.buscarPessoaPoridPessoa(idPessoa);
        if(!ObjectUtils.isEmpty(pessoaDoBanco)) {
            pessoaMapper.updateEntityFromDTO(pessoaDTO, pessoaDoBanco);
            pessoaService.atualizarPessoa(pessoaDoBanco);
        }
        return defaultHandleResponse.responseHandler(pessoaMapper.toDTO(pessoaDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarPessoa(Long idPessoa) {
        Pessoa pessoa = pessoaService.buscarPessoaPoridPessoa(idPessoa);
        if(!ObjectUtils.isEmpty(pessoa)) pessoaService.deletarPessoa(pessoa);
        return defaultHandleResponse.responseHandler(pessoa, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
