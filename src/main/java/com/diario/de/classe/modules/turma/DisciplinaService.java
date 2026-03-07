package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {
    private final DisciplinaRepository repository;

    public DisciplinaService(DisciplinaRepository repository) { this.repository = repository; }

    public List<Disciplina> buscarTodos() { return repository.findAll(); }

    public Disciplina buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com id: " + id));
    }

    public Disciplina criar(Disciplina disciplina) { return repository.save(disciplina); }

    public Disciplina atualizar(Long id, Disciplina dados) {
        Disciplina existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idDisciplina", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
