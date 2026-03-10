package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {
    private final TurmaRepository repository;

    public TurmaService(TurmaRepository repository) { this.repository = repository; }

    public List<Turma> buscarTodos() { return repository.findAll(); }

    public Turma buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + id));
    }

    public Turma criar(Turma turma) { return repository.save(turma); }

    public Turma atualizar(Long id, Turma dados) {
        Turma existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idTurma", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
