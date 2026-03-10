package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoService {

    private final ContatoRepository repository;

    public ContatoService(ContatoRepository repository) {
        this.repository = repository;
    }

    public List<Contato> buscarTodos() {
        return repository.findAll();
    }

    public Contato buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com id: " + id));
    }

    public Contato criar(Contato contato) {
        return repository.save(contato);
    }

    public Contato atualizar(Long id, Contato dados) {
        Contato existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idContato", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Contato existente = buscarPorId(id);
        repository.delete(existente);
    }
}
