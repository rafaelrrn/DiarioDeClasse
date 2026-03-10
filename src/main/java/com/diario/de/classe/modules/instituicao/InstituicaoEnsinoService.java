package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituicaoEnsinoService {

    private final InstituicaoEnsinoRepository repository;

    public InstituicaoEnsinoService(InstituicaoEnsinoRepository repository) {
        this.repository = repository;
    }

    public List<InstituicaoEnsino> buscarTodos() { return repository.findAll(); }

    public InstituicaoEnsino buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InstituicaoEnsino não encontrada com id: " + id));
    }

    public InstituicaoEnsino criar(InstituicaoEnsino entity) { return repository.save(entity); }

    public InstituicaoEnsino atualizar(Long id, InstituicaoEnsino dados) {
        InstituicaoEnsino existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idInstituicaoEnsino", "createdAt");
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
