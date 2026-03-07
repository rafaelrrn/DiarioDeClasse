package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesService {
    private final MesRepository repository;

    public MesService(MesRepository repository) { this.repository = repository; }

    public List<Mes> buscarTodos() { return repository.findAll(); }

    public Mes buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mês não encontrado com id: " + id));
    }

    public Mes criar(Mes mes) { return repository.save(mes); }

    public Mes atualizar(Long id, Mes dados) {
        Mes existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idMes", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
