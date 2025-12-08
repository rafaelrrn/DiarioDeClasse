package com.diario.de.classe.facade.Impl;

import com.diario.de.classe.dto.SerieDTO;
import com.diario.de.classe.facade.SerieFacade;
import com.diario.de.classe.model.Mes;
import com.diario.de.classe.model.Serie;
import com.diario.de.classe.response.ResponseDiarioDeClasse;
import com.diario.de.classe.service.SerieService;
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
public class SerieFacadeImpl implements SerieFacade {
    final private static Logger LOG = LogManager.getLogger(SerieFacadeImpl.class);

    @Value("${geral.erro.nao.encontrado.msg}")
    private String NOT_FOUND_MSG;
    @Value("${geral.erro.criar.msg}")
    private String ERRO_AO_CRIAR_MSG;
    @Value("${geral.erro.atualizar.msg}")
    private String ERRO_AO_ATUALIZAR_MSG;

    @Autowired
    SerieService serieService;
    @Autowired
    ResponseDiarioDeClasse responseDiarioDeClasse;
    @Autowired
    private DefaultResponseHandleUtil defaultHandleResponse;

    @Override
    public ResponseEntity<Object> buscarTodasSeries() {
        List<Serie> entities = serieService.buscarTodasSeries();
        List<SerieDTO> dtos = entities.stream().map(SerieDTO::new).toList();
        return defaultHandleResponse.responseHandler(dtos, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> buscarSeriePoridSerie(Long idSerie) {
        Serie entity = serieService.buscarSeriePoridSerie(idSerie);
        SerieDTO dto = entity != null ? new SerieDTO(entity) : null;
        return defaultHandleResponse.responseHandler(dto, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> criarSerie(SerieDTO serieDTO) {
        Serie entity = new Serie();
        BeanUtils.copyProperties(serieDTO, entity);
        Serie salvo = serieService.criarSerie(entity);
        return defaultHandleResponse.responseHandler(new SerieDTO(salvo), HttpStatus.INTERNAL_SERVER_ERROR, ERRO_AO_CRIAR_MSG);
    }

    @Override
    public ResponseEntity<Object> atualizarSerie(Long idSerie, SerieDTO serieDTO) {
        Serie entity = serieService.buscarSeriePoridSerie(idSerie);
        if(!ObjectUtils.isEmpty(entity)) {
            Serie atualizado = new Serie();
            BeanUtils.copyProperties(serieDTO, atualizado);
            serieService.atualizarSerie(idSerie, atualizado, entity);
        }
        return defaultHandleResponse.responseHandler(new SerieDTO(entity), HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

    @Override
    public ResponseEntity<Object> deletarSerie(Long idSerie) {
        Serie serie = serieService.buscarSeriePoridSerie(idSerie);
        if(!ObjectUtils.isEmpty(serie)) serieService.deletarSerie(serie);
        return defaultHandleResponse.responseHandler(serie, HttpStatus.NOT_FOUND, NOT_FOUND_MSG);
    }

}
