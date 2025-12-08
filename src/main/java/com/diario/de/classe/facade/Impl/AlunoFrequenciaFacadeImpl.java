package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.AlunoFrequenciaDTO;
import com.diario.de.classe.facade.AlunoFrequenciaFacade;
import com.diario.de.classe.model.AlunoFrequencia;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.AlunoFrequenciaService;
import com.diario.de.classe.util.DefaultResponseHandleUtil;
import com.diario.de.classe.mapper.AlunoFrequenciaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AlunoFrequenciaFacadeImpl implements AlunoFrequenciaFacade {
    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    AlunoFrequenciaService alunoFrequenciaService;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;
    @Autowired
    private AlunoFrequenciaMapper alunoFrequenciaMapper;

    @Override
    public ResponseEntity<Object> buscarTodosAlunosFrequencias() {
        List<AlunoFrequencia> entities = alunoFrequenciaService.buscarTodosAlunosFrequencias();
        List<AlunoFrequenciaDTO> dtos = entities.stream().map(AlunoFrequenciaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarAlunoFrequenciaPorIdAlunoFrequencia(Long IdAlunoFrequencia) {
        AlunoFrequencia entity = alunoFrequenciaService.buscarAlunoFrequenciaPorIdAlunoFrequencia(IdAlunoFrequencia);
        AlunoFrequenciaDTO dto = entity != null ? new AlunoFrequenciaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarAlunoFrequencia(AlunoFrequenciaDTO alunoFrequenciaDTO) {
        AlunoFrequencia entity = alunoFrequenciaMapper.toEntity(alunoFrequenciaDTO);
        AlunoFrequencia salvo = alunoFrequenciaService.criarAlunoFrequencia(entity);
        return defaultHandleResponse.responseHandler(alunoFrequenciaMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarAlunoFrequencia(Long idAlunoFrequencia, AlunoFrequenciaDTO alunoFrequenciaDTO) {
        AlunoFrequencia entity = alunoFrequenciaService.buscarAlunoFrequenciaPorIdAlunoFrequencia(idAlunoFrequencia);
        if(!ObjectUtils.isEmpty(entity)) {
            alunoFrequenciaMapper.updateEntityFromDTO(alunoFrequenciaDTO, entity);
            alunoFrequenciaService.atualizarAlunoFrequencia(entity);
        }
        return defaultHandleResponse.responseHandler(alunoFrequenciaMapper.toDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarAlunoFrequencia(Long IdAlunoFrequencia) {
        AlunoFrequencia entity = alunoFrequenciaService.buscarAlunoFrequenciaPorIdAlunoFrequencia(IdAlunoFrequencia);
        if(!ObjectUtils.isEmpty(entity)) alunoFrequenciaService.deletarAlunoFrequencia(entity);
        return defaultHandleResponse.responseHandler(new AlunoFrequenciaDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
