package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoPessoaService {

    private final ContatoPessoaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final ContatoRepository contatoRepository;

    public ContatoPessoaService(ContatoPessoaRepository repository,
                                 PessoaRepository pessoaRepository,
                                 ContatoRepository contatoRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.contatoRepository = contatoRepository;
    }

    public List<ContatoPessoa> buscarTodos() {
        return repository.findAll();
    }

    public ContatoPessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContatoPessoa não encontrado com id: " + id));
    }

    public ContatoPessoa criar(ContatoPessoa contatoPessoa, Long idPessoa, Long idContato) {
        contatoPessoa.setPessoa(pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + idPessoa)));
        contatoPessoa.setContato(contatoRepository.findById(idContato)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado com id: " + idContato)));
        return repository.save(contatoPessoa);
    }

    public ContatoPessoa atualizar(Long id, ContatoPessoa dados) {
        ContatoPessoa existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idContatoPessoa", "createdAt", "pessoa", "contato");
        return repository.save(existente);
    }

    public void deletar(Long id) {
        ContatoPessoa existente = buscarPorId(id);
        repository.delete(existente);
    }
}
