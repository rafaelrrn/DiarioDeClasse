package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository repository;
    private final EnsinoRepository ensinoRepository;
    private final GrauRepository grauRepository;
    private final SerieRepository serieRepository;

    public CursoService(CursoRepository repository, EnsinoRepository ensinoRepository,
                        GrauRepository grauRepository, SerieRepository serieRepository) {
        this.repository = repository;
        this.ensinoRepository = ensinoRepository;
        this.grauRepository = grauRepository;
        this.serieRepository = serieRepository;
    }

    public List<Curso> buscarTodos() { return repository.findAll(); }

    public Curso buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id: " + id));
    }

    public Curso criar(Curso curso, Long idEnsino, Long idGrau, Long idSerie) {
        curso.setEnsino(ensinoRepository.findById(idEnsino)
                .orElseThrow(() -> new ResourceNotFoundException("Ensino não encontrado com id: " + idEnsino)));
        curso.setGrau(grauRepository.findById(idGrau)
                .orElseThrow(() -> new ResourceNotFoundException("Grau não encontrado com id: " + idGrau)));
        curso.setSerie(serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException("Serie não encontrada com id: " + idSerie)));
        return repository.save(curso);
    }

    public Curso atualizar(Long id, Curso dados, Long idEnsino, Long idGrau, Long idSerie) {
        Curso existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idCurso", "createdAt", "ensino", "grau", "serie");
        if (idEnsino != null) existente.setEnsino(ensinoRepository.findById(idEnsino)
                .orElseThrow(() -> new ResourceNotFoundException("Ensino não encontrado com id: " + idEnsino)));
        if (idGrau != null) existente.setGrau(grauRepository.findById(idGrau)
                .orElseThrow(() -> new ResourceNotFoundException("Grau não encontrado com id: " + idGrau)));
        if (idSerie != null) existente.setSerie(serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException("Serie não encontrada com id: " + idSerie)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
