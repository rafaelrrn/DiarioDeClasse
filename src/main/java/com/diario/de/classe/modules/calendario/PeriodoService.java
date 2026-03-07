package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodoService {
    private final PeriodoRepository repository;

    public PeriodoService(PeriodoRepository repository) { this.repository = repository; }

    public List<Periodo> buscarTodos() { return repository.findAll(); }

    public Periodo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Período não encontrado com id: " + id));
    }

    public Periodo criar(Periodo periodo) { return repository.save(periodo); }

    public Periodo atualizar(Long id, Periodo dados) {
        Periodo existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idPeriodo", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
