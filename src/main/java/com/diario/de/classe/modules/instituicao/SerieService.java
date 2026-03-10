package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {
    private final SerieRepository repository;
    public SerieService(SerieRepository repository) { this.repository = repository; }
    public List<Serie> buscarTodos() { return repository.findAll(); }
    public Serie buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Serie não encontrada com id: " + id));
    }
    public Serie criar(Serie entity) { return repository.save(entity); }
    public Serie atualizar(Long id, Serie dados) {
        Serie existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idSerie", "createdAt");
        return repository.save(existente);
    }
    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
