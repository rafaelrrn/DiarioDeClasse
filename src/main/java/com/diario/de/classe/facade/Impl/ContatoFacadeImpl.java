package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.facade.ComponenteCurricularFacade;
import com.diario.de.classe.facade.ContatoFacade;
import com.diario.de.classe.model.ComponenteCurricular;
import com.diario.de.classe.model.Contato;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.ContatoService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.dto.ContatoDTO;
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
public class ContatoFacadeImpl implements ContatoFacade {
    final private static Logger LOG = LogManager.getLogger(ContatoFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private ContatoService contatoService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosContatos() {
        List<Contato> entities = contatoService.buscarTodosContatos();
        List<ContatoDTO> dtos = entities.stream().map(ContatoDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarContatoPoridContato(Long idContato) {
        Contato entity = contatoService.buscarContatoPoridContato(idContato);
        ContatoDTO dto = entity != null ? new ContatoDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarContato(ContatoDTO contatoDTO) {
        Contato entity = new Contato();
        BeanUtils.copyProperties(contatoDTO, entity);
        Contato salvo = contatoService.criarContato(entity);
        return defaultHandleResponse.responseHandler(new ContatoDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarContato(Long idContato, ContatoDTO contatoDTO) {
        Contato entity = contatoService.buscarContatoPoridContato(idContato);
        if(!ObjectUtils.isEmpty(entity)) {
            Contato atualizado = new Contato();
            BeanUtils.copyProperties(contatoDTO, atualizado);
            contatoService.atualizarContato(idContato, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new ContatoDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarContato(Long idContato) {
        Contato contato = contatoService.buscarContatoPoridContato(idContato);
        if(!ObjectUtils.isEmpty(contato)) contatoService.deletarContato(contato);
        return defaultHandleResponse.responseHandler(contato, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
