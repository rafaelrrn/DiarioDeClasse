package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository repository;

    public EnderecoService(EnderecoRepository repository) {
        this.repository = repository;
    }

    public List<Endereco> buscarTodos() {
        return repository.findAll();
    }

    public Endereco buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado com id: " + id));
    }

    public Endereco criar(Endereco endereco) {
        return repository.save(endereco);
    }

    public Endereco atualizar(Long id, Endereco dados) {
        Endereco existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idEndereco", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Endereco existente = buscarPorId(id);
        repository.delete(existente);
    }
}
