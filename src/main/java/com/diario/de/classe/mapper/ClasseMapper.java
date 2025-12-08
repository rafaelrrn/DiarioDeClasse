package com.diario.de.classe.mapper;

import com.diario.de.classe.dto.ClasseDTO;
import com.diario.de.classe.model.Classe;
import com.diario.de.classe.service.ComponenteCurricularService;
import com.diario.de.classe.service.CursoService;
import com.diario.de.classe.service.InstituicaoEnsinoService;
import com.diario.de.classe.service.PessoaService;
import com.diario.de.classe.service.TurmaService;
import com.diario.de.classe.service.TurnoService;
import org.springframework.stereotype.Component;

@Component
public class ClasseMapper {

    private final InstituicaoEnsinoService instituicaoEnsinoService;

    private final ComponenteCurricularService componenteCurricularService;

    private final CursoService cursoService;

    private final TurnoService turnoService;

    private final TurmaService turmaService;

    private final PessoaService pessoaService;

    public ClasseMapper(InstituicaoEnsinoService instituicaoEnsinoService, 
                       ComponenteCurricularService componenteCurricularService,
                       CursoService cursoService,
                       TurnoService turnoService,
                       TurmaService turmaService,
                       PessoaService pessoaService){

        this.instituicaoEnsinoService = instituicaoEnsinoService;
        this.componenteCurricularService = componenteCurricularService;
        this.cursoService = cursoService;
        this.turnoService = turnoService;
        this.turmaService = turmaService;
        this.pessoaService = pessoaService;

    }

    public Classe toEntity(ClasseDTO classeDTO){
        if (classeDTO == null){
            return null;
        }

        Classe entity = new Classe();
        entity.setIdClasse(classeDTO.getIdClasse());

        if (classeDTO.getIdInstituicaoEnsino() != null){
            entity.setInstituicaoEnsino(
                    instituicaoEnsinoService.buscarInstituicaoEnsinoPoridInstituicaoEnsino(classeDTO.getIdInstituicaoEnsino())
            );
        }

        if (classeDTO.getIdComponenteCurricular() != null){
            entity.setComponenteCurricular(
                    componenteCurricularService.buscarComponenteCurricularPoridComponenteCurricular(classeDTO.getIdComponenteCurricular())
            );
        }

        if (classeDTO.getIdCurso() != null){
            entity.setCurso(
                    cursoService.buscarCursoPoridCurso(classeDTO.getIdCurso())
            );
        }

        if (classeDTO.getIdTurno() != null){
            entity.setTurno(
                    turnoService.buscarTurnoPoridTurno(classeDTO.getIdTurno())
            );
        }

        if (classeDTO.getIdTurma() != null){
            entity.setTurma(
                    turmaService.buscarTurmaPoridTurma(classeDTO.getIdTurma())
            );
        }

        if (classeDTO.getIdProfessor() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(classeDTO.getIdProfessor())
            );
        }

        return entity;
    }

    public ClasseDTO toDTO(Classe entity){
        if (entity == null){
            return null;
        }

        ClasseDTO classeDTO = new ClasseDTO();
        classeDTO.setIdClasse(entity.getIdClasse());

        classeDTO.setIdInstituicaoEnsino(
                entity.getInstituicaoEnsino() != null ? entity.getInstituicaoEnsino().getIdInstituicaoEnsino() : null
        );

        classeDTO.setIdComponenteCurricular(
                entity.getComponenteCurricular() != null ? entity.getComponenteCurricular().getIdComponenteCurricular() : null
        );

        classeDTO.setIdCurso(
                entity.getCurso() != null ? entity.getCurso().getIdCurso() : null
        );

        classeDTO.setIdTurno(
                entity.getTurno() != null ? entity.getTurno().getIdTurno() : null
        );

        classeDTO.setIdTurma(
                entity.getTurma() != null ? entity.getTurma().getIdTurma() : null
        );

        classeDTO.setIdProfessor(
                entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null
        );

        classeDTO.setCreatedAt(entity.getCreatedAt());
        classeDTO.setUpdatedAt(entity.getUpdatedAt());

        return classeDTO;
    }

    public void updateEntityFromDTO(ClasseDTO classeDTO, Classe entity){
        if (classeDTO == null || entity == null){
            return;
        }

        if (classeDTO.getIdInstituicaoEnsino() != null){
            entity.setInstituicaoEnsino(
                    instituicaoEnsinoService.buscarInstituicaoEnsinoPoridInstituicaoEnsino(classeDTO.getIdInstituicaoEnsino())
            );
        }

        if (classeDTO.getIdComponenteCurricular() != null){
            entity.setComponenteCurricular(
                    componenteCurricularService.buscarComponenteCurricularPoridComponenteCurricular(classeDTO.getIdComponenteCurricular())
            );
        }

        if (classeDTO.getIdCurso() != null){
            entity.setCurso(
                    cursoService.buscarCursoPoridCurso(classeDTO.getIdCurso())
            );
        }

        if (classeDTO.getIdTurno() != null){
            entity.setTurno(
                    turnoService.buscarTurnoPoridTurno(classeDTO.getIdTurno())
            );
        }

        if (classeDTO.getIdTurma() != null){
            entity.setTurma(
                    turmaService.buscarTurmaPoridTurma(classeDTO.getIdTurma())
            );
        }

        if (classeDTO.getIdProfessor() != null){
            entity.setPessoa(
                    pessoaService.buscarPessoaPoridPessoa(classeDTO.getIdProfessor())
            );
        }
    }
}
