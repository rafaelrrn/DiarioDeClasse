package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService {
    private final TurnoRepository repository;
    public TurnoService(TurnoRepository repository) { this.repository = repository; }
    public List<Turno> buscarTodos() { return repository.findAll(); }
    public Turno buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Turno não encontrado com id: " + id));
    }
    public Turno criar(Turno entity) { return repository.save(entity); }
    public Turno atualizar(Long id, Turno dados) {
        Turno existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idTurno", "createdAt");
        return repository.save(existente);
    }
    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
