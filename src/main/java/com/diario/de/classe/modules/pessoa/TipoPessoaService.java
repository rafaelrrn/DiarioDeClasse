package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsável pelas regras de negócio de TipoPessoa.
 * Toda lógica de negócio deve residir aqui — nunca no Controller.
 */
@Service
public class TipoPessoaService {

    private final TipoPessoaRepository repository;

    public TipoPessoaService(TipoPessoaRepository repository) {
        this.repository = repository;
    }

    /** Retorna todos os tipos de pessoa cadastrados. */
    public List<TipoPessoa> buscarTodos() {
        return repository.findAll();
    }

    /**
     * Busca um TipoPessoa pelo ID.
     * @throws ResourceNotFoundException se não encontrado
     */
    public TipoPessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPessoa não encontrado com id: " + id));
    }

    /** Persiste um novo TipoPessoa. */
    public TipoPessoa criar(TipoPessoa tipoPessoa) {
        return repository.save(tipoPessoa);
    }

    /**
     * Atualiza os dados de um TipoPessoa existente.
     * Copia apenas os campos não nulos do DTO para a entidade do banco.
     */
    public TipoPessoa atualizar(Long id, TipoPessoa dados) {
        TipoPessoa existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idTipoPessoa", "createdAt");
        return repository.save(existente);
    }

    /**
     * Remove um TipoPessoa pelo ID.
     * TODO (Etapa 2): Substituir por soft delete (ativo = false).
     */
    public void deletar(Long id) {
        TipoPessoa existente = buscarPorId(id);
        repository.delete(existente);
    }
}
