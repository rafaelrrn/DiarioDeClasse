package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnsinoService {
    private final EnsinoRepository repository;
    public EnsinoService(EnsinoRepository repository) { this.repository = repository; }
    public List<Ensino> buscarTodos() { return repository.findAll(); }
    public Ensino buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ensino não encontrado com id: " + id));
    }
    public Ensino criar(Ensino entity) { return repository.save(entity); }
    public Ensino atualizar(Long id, Ensino dados) {
        Ensino existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idEnsino", "createdAt");
        return repository.save(existente);
    }
    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
