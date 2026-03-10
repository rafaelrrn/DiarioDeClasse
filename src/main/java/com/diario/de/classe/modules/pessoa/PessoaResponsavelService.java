package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaResponsavelService {

    private final PessoaResponsavelRepository repository;
    private final PessoaRepository pessoaRepository;

    public PessoaResponsavelService(PessoaResponsavelRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<PessoaResponsavel> buscarTodos() {
        return repository.findAll();
    }

    public PessoaResponsavel buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PessoaResponsavel não encontrado com id: " + id));
    }

    public PessoaResponsavel criar(PessoaResponsavel pessoaResponsavel, Long idAluno, Long idResponsavel) {
        pessoaResponsavel.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        pessoaResponsavel.setPessoaResponsavel(pessoaRepository.findById(idResponsavel)
                .orElseThrow(() -> new ResourceNotFoundException("Responsavel não encontrado com id: " + idResponsavel)));
        return repository.save(pessoaResponsavel);
    }

    public PessoaResponsavel atualizar(Long id, PessoaResponsavel dados) {
        PessoaResponsavel existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idPessoaResponsavel", "createdAt", "pessoaAluno", "pessoaResponsavel");
        return repository.save(existente);
    }

    public void deletar(Long id) {
        PessoaResponsavel existente = buscarPorId(id);
        repository.delete(existente);
    }
}
