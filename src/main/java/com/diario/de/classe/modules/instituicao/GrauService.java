package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrauService {
    private final GrauRepository repository;
    public GrauService(GrauRepository repository) { this.repository = repository; }
    public List<Grau> buscarTodos() { return repository.findAll(); }
    public Grau buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Grau não encontrado com id: " + id));
    }
    public Grau criar(Grau entity) { return repository.save(entity); }
    public Grau atualizar(Long id, Grau dados) {
        Grau existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idGrau", "createdAt");
        return repository.save(existente);
    }
    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
