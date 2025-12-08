package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.AlunoTurmaDTO;
import com.diario.de.classe.facade.AlunoTurmaFacade;
import com.diario.de.classe.mapper.AlunoTurmaMapper;
import com.diario.de.classe.model.AlunoTurma;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.AlunoTurmaService;
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
public class AlunoTurmaFacadeImpl implements AlunoTurmaFacade {
    final private static Logger LOG = LogManager.getLogger(AlunoTurmaFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    private AlunoTurmaMapper alunoTurmaMapper;
    @Autowired
    private AlunoTurmaService alunoTurmaService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodosAlunosTurmas() {
        List<AlunoTurma> entities = alunoTurmaService.buscarTodosAlunosTurmas();
        List<AlunoTurmaDTO> dtos = entities.stream().map(AlunoTurmaDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarAlunoTurmaPorIdAlunoTurma(Long idAlunoTurma) {
        AlunoTurma entity = alunoTurmaService.buscarAlunoTurmaPorIdAlunoTurma(idAlunoTurma);
        AlunoTurmaDTO dto = entity != null ? new AlunoTurmaDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarAlunoTurma(AlunoTurmaDTO alunoTurmaDTO) {
        AlunoTurma entity = alunoTurmaMapper.toEntity(alunoTurmaDTO);
        AlunoTurma salvo = alunoTurmaService.criarAlunoTurma(entity);
        return defaultHandleResponse.responseHandler(alunoTurmaMapper.toDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarAlunoTurma(Long idAlunoTurma, AlunoTurmaDTO alunoTurmaDTO) {
        AlunoTurma alunoTurmaDoBanco = alunoTurmaService.buscarAlunoTurmaPorIdAlunoTurma(idAlunoTurma);
        if(!ObjectUtils.isEmpty(alunoTurmaDoBanco)) {
            alunoTurmaMapper.updateEntityFromDTO(alunoTurmaDTO, alunoTurmaDoBanco);
            alunoTurmaService.atualizarAlunoTurma(alunoTurmaDoBanco);
        }
        return defaultHandleResponse.responseHandler(alunoTurmaMapper.toDTO(alunoTurmaDoBanco), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarAlunoTurma(Long idAlunoTurma) {
        AlunoTurma alunoTurma = alunoTurmaService.buscarAlunoTurmaPorIdAlunoTurma(idAlunoTurma);
        if(!ObjectUtils.isEmpty(alunoTurma)) alunoTurmaService.deletarAlunoTurma(alunoTurma);
        return defaultHandleResponse.responseHandler(alunoTurma, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
