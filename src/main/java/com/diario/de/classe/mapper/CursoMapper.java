package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.CursoDTO;
import com.diario.de.classe.model.Curso;
import com.diario.de.classe.service.EnsinoService;
import com.diario.de.classe.service.GrauService;
import com.diario.de.classe.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    private final EnsinoService ensinoService;

    private final GrauService grauService;

    private final SerieService serieService;

    @Autowired
    public CursoMapper(EnsinoService ensinoService, GrauService grauService, SerieService serieService){

        this.ensinoService = ensinoService;
        this.grauService = grauService;
        this.serieService = serieService;

    }

    public Curso toEntity(CursoDTO cursoDTO){
        if (cursoDTO == null){
            return null;
        }

        Curso entity = new Curso();
        entity.setIdCurso(cursoDTO.getIdCurso());

        if (cursoDTO.getIdEnsino() != null){
            entity.setEnsino(
                    ensinoService.buscarEnsinoPoridEnsino(cursoDTO.getIdEnsino())
            );
        }

        if (cursoDTO.getIdGrau() != null){
            entity.setGrau(
                    grauService.buscarGrauPoridGrau(cursoDTO.getIdGrau())
            );
        }

        if (cursoDTO.getIdSerie() != null){
            entity.setSerie(
                    serieService.buscarSeriePoridSerie(cursoDTO.getIdSerie())
            );
        }

        return entity;
    }

    public CursoDTO toDTO(Curso entity){
        if (entity == null){
            return null;
        }

        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setIdCurso(entity.getIdCurso());

        cursoDTO.setIdEnsino(
                entity.getEnsino() != null ? entity.getEnsino().getIdEnsino() : null
        );

        cursoDTO.setIdGrau(
                entity.getGrau() != null ? entity.getGrau().getIdGrau() : null
        );

        cursoDTO.setIdSerie(
                entity.getSerie() != null ? entity.getSerie().getIdSerie() : null
        );

        return cursoDTO;
    }

    public void updateEntityFromDTO(CursoDTO cursoDTO, Curso entity){
        if (cursoDTO == null || entity == null){
            return;
        }

        if (cursoDTO.getIdEnsino() != null){
            entity.setEnsino(
                    ensinoService.buscarEnsinoPoridEnsino(cursoDTO.getIdEnsino())
            );
        }

        if (cursoDTO.getIdGrau() != null){
            entity.setGrau(
                    grauService.buscarGrauPoridGrau(cursoDTO.getIdGrau())
            );
        }

        if (cursoDTO.getIdSerie() != null){
            entity.setSerie(
                    serieService.buscarSeriePoridSerie(cursoDTO.getIdSerie())
            );
        }
    }
}
