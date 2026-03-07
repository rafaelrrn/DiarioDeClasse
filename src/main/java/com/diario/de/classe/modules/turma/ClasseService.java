package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.instituicao.CursoRepository;
import com.diario.de.classe.modules.instituicao.InstituicaoEnsinoRepository;
import com.diario.de.classe.modules.instituicao.TurnoRepository;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasseService {
    private final ClasseRepository repository;
    private final InstituicaoEnsinoRepository instituicaoEnsinoRepository;
    private final ComponenteCurricularRepository componenteCurricularRepository;
    private final CursoRepository cursoRepository;
    private final TurnoRepository turnoRepository;
    private final TurmaRepository turmaRepository;
    private final PessoaRepository pessoaRepository;

    public ClasseService(ClasseRepository repository,
                         InstituicaoEnsinoRepository instituicaoEnsinoRepository,
                         ComponenteCurricularRepository componenteCurricularRepository,
                         CursoRepository cursoRepository,
                         TurnoRepository turnoRepository,
                         TurmaRepository turmaRepository,
                         PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.instituicaoEnsinoRepository = instituicaoEnsinoRepository;
        this.componenteCurricularRepository = componenteCurricularRepository;
        this.cursoRepository = cursoRepository;
        this.turnoRepository = turnoRepository;
        this.turmaRepository = turmaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Classe> buscarTodos() { return repository.findAll(); }

    public Classe buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com id: " + id));
    }

    public Classe criar(Classe classe, Long idInstituicaoEnsino, Long idComponenteCurricular,
                        Long idCurso, Long idTurno, Long idTurma, Long idProfessor) {
        classe.setInstituicaoEnsino(instituicaoEnsinoRepository.findById(idInstituicaoEnsino)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição de ensino não encontrada com id: " + idInstituicaoEnsino)));
        if (idComponenteCurricular != null) {
            classe.setComponenteCurricular(componenteCurricularRepository.findById(idComponenteCurricular)
                    .orElseThrow(() -> new ResourceNotFoundException("Componente curricular não encontrado com id: " + idComponenteCurricular)));
        }
        classe.setCurso(cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id: " + idCurso)));
        classe.setTurno(turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("Turno não encontrado com id: " + idTurno)));
        classe.setTurma(turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + idTurma)));
        classe.setPessoa(pessoaRepository.findById(idProfessor)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com id: " + idProfessor)));
        return repository.save(classe);
    }

    public Classe atualizar(Long id, Long idInstituicaoEnsino, Long idComponenteCurricular,
                            Long idCurso, Long idTurno, Long idTurma, Long idProfessor) {
        Classe existente = buscarPorId(id);
        if (idInstituicaoEnsino != null) existente.setInstituicaoEnsino(instituicaoEnsinoRepository.findById(idInstituicaoEnsino)
                .orElseThrow(() -> new ResourceNotFoundException("Instituição de ensino não encontrada com id: " + idInstituicaoEnsino)));
        if (idComponenteCurricular != null) existente.setComponenteCurricular(componenteCurricularRepository.findById(idComponenteCurricular)
                .orElseThrow(() -> new ResourceNotFoundException("Componente curricular não encontrado com id: " + idComponenteCurricular)));
        if (idCurso != null) existente.setCurso(cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id: " + idCurso)));
        if (idTurno != null) existente.setTurno(turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("Turno não encontrado com id: " + idTurno)));
        if (idTurma != null) existente.setTurma(turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + idTurma)));
        if (idProfessor != null) existente.setPessoa(pessoaRepository.findById(idProfessor)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com id: " + idProfessor)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
