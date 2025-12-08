package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.CalendarioEscolarDTO;
import com.diario.de.classe.model.CalendarioEscolar;
import com.diario.de.classe.service.MesService;
import com.diario.de.classe.service.AnoCalendarioService;
import com.diario.de.classe.service.PeriodoService;
import com.diario.de.classe.service.ClasseService;
import org.springframework.stereotype.Component;

@Component
public class CalendarioEscolarMapper {

    private final MesService mesService;

    private final AnoCalendarioService anoCalendarioService;

    private final PeriodoService periodoService;

    private final ClasseService classeService;

    public CalendarioEscolarMapper(MesService mesService, AnoCalendarioService anoCalendarioService, PeriodoService periodoService, ClasseService classeService){

        this.mesService = mesService;
        this.anoCalendarioService = anoCalendarioService;
        this.periodoService = periodoService;
        this.classeService = classeService;

    }

    public CalendarioEscolar toEntity(CalendarioEscolarDTO calendarioEscolarDTO){
        if (calendarioEscolarDTO == null){
            return null;
        }

        CalendarioEscolar entity = new CalendarioEscolar();
        entity.setIdCalendarioEscolar(calendarioEscolarDTO.getIdCalendarioEscolar());

        if (calendarioEscolarDTO.getIdMes() != null){
            entity.setMes(
                    mesService.buscarMesPoridMes(calendarioEscolarDTO.getIdMes())
            );
        }

        if (calendarioEscolarDTO.getIdAnoCalendario() != null){
            entity.setAnoCalendario(
                    anoCalendarioService.buscarAnoCalendarioPoridAnoCalendario(calendarioEscolarDTO.getIdAnoCalendario())
            );
        }

        if (calendarioEscolarDTO.getIdPeriodo() != null){
            entity.setPeriodo(
                    periodoService.buscarPeriodoPoridPeriodo(calendarioEscolarDTO.getIdPeriodo())
            );
        }

        if (calendarioEscolarDTO.getIdClasse() != null){
            entity.setClasse(
                    classeService.buscarClassePoridClasse(calendarioEscolarDTO.getIdClasse())
            );
        }

        entity.setDiasLetivos(calendarioEscolarDTO.getDiasLetivos());
        entity.setDiasAvaliacoes(calendarioEscolarDTO.getDiasAvaliacoes());

        return entity;
    }

    public CalendarioEscolarDTO toDTO(CalendarioEscolar entity){
        if (entity == null){
            return null;
        }

        CalendarioEscolarDTO calendarioEscolarDTO = new CalendarioEscolarDTO();
        calendarioEscolarDTO.setIdCalendarioEscolar(entity.getIdCalendarioEscolar());

        calendarioEscolarDTO.setIdMes(
                entity.getMes() != null ? entity.getMes().getIdMes() : null
        );

        calendarioEscolarDTO.setIdAnoCalendario(
                entity.getAnoCalendario() != null ? entity.getAnoCalendario().getIdAnoCalendario() : null
        );

        calendarioEscolarDTO.setIdPeriodo(
                entity.getPeriodo() != null ? entity.getPeriodo().getIdPeriodo() : null
        );

        calendarioEscolarDTO.setIdClasse(
                entity.getClasse() != null ? entity.getClasse().getIdClasse() : null
        );

        calendarioEscolarDTO.setDiasLetivos(entity.getDiasLetivos());
        calendarioEscolarDTO.setDiasAvaliacoes(entity.getDiasAvaliacoes());

        return calendarioEscolarDTO;
    }

    public void updateEntityFromDTO(CalendarioEscolarDTO calendarioEscolarDTO, CalendarioEscolar entity){
        if (calendarioEscolarDTO == null || entity == null){
            return;
        }

        if (calendarioEscolarDTO.getIdMes() != null){
            entity.setMes(
                    mesService.buscarMesPoridMes(calendarioEscolarDTO.getIdMes())
            );
        }

        if (calendarioEscolarDTO.getIdAnoCalendario() != null){
            entity.setAnoCalendario(
                    anoCalendarioService.buscarAnoCalendarioPoridAnoCalendario(calendarioEscolarDTO.getIdAnoCalendario())
            );
        }

        if (calendarioEscolarDTO.getIdPeriodo() != null){
            entity.setPeriodo(
                    periodoService.buscarPeriodoPoridPeriodo(calendarioEscolarDTO.getIdPeriodo())
            );
        }

        if (calendarioEscolarDTO.getIdClasse() != null){
            entity.setClasse(
                    classeService.buscarClassePoridClasse(calendarioEscolarDTO.getIdClasse())
            );
        }

        if (calendarioEscolarDTO.getDiasLetivos() != null){
            entity.setDiasLetivos(calendarioEscolarDTO.getDiasLetivos());
        }

        if (calendarioEscolarDTO.getDiasAvaliacoes() != null){
            entity.setDiasAvaliacoes(calendarioEscolarDTO.getDiasAvaliacoes());
        }
    }
}
