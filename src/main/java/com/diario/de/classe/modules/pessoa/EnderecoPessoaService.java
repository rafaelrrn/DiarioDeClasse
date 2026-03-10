package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoPessoaService {

    private final EnderecoPessoaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    public EnderecoPessoaService(EnderecoPessoaRepository repository,
                                  PessoaRepository pessoaRepository,
                                  EnderecoRepository enderecoRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public List<EnderecoPessoa> buscarTodos() {
        return repository.findAll();
    }

    public EnderecoPessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EnderecoPessoa não encontrado com id: " + id));
    }

    public EnderecoPessoa criar(EnderecoPessoa enderecoPessoa, Long idPessoa, Long idEndereco) {
        enderecoPessoa.setPessoa(pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + idPessoa)));
        enderecoPessoa.setEndereco(enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado com id: " + idEndereco)));
        return repository.save(enderecoPessoa);
    }

    public EnderecoPessoa atualizar(Long id, EnderecoPessoa dados) {
        EnderecoPessoa existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idEnderecoPessoa", "createdAt", "pessoa", "endereco");
        return repository.save(existente);
    }

    public void deletar(Long id) {
        EnderecoPessoa existente = buscarPorId(id);
        repository.delete(existente);
    }
}
