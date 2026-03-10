package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponenteCurricularService {
    private final ComponenteCurricularRepository repository;

    public ComponenteCurricularService(ComponenteCurricularRepository repository) { this.repository = repository; }

    public List<ComponenteCurricular> buscarTodos() { return repository.findAll(); }

    public ComponenteCurricular buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Componente curricular não encontrado com id: " + id));
    }

    public ComponenteCurricular criar(ComponenteCurricular componenteCurricular) { return repository.save(componenteCurricular); }

    public ComponenteCurricular atualizar(Long id, ComponenteCurricular dados) {
        ComponenteCurricular existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idComponenteCurricular", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
