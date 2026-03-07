package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnoCalendarioService {
    private final AnoCalendarioRepository repository;

    public AnoCalendarioService(AnoCalendarioRepository repository) { this.repository = repository; }

    public List<AnoCalendario> buscarTodos() { return repository.findAll(); }

    public AnoCalendario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ano calendário não encontrado com id: " + id));
    }

    public AnoCalendario criar(AnoCalendario anoCalendario) { return repository.save(anoCalendario); }

    public AnoCalendario atualizar(Long id, AnoCalendario dados) {
        AnoCalendario existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idAnoCalendario", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
