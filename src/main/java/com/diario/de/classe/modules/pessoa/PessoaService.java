package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    private final PessoaRepository repository;
    private final TipoPessoaRepository tipoPessoaRepository;

    public PessoaService(PessoaRepository repository, TipoPessoaRepository tipoPessoaRepository) {
        this.repository = repository;
        this.tipoPessoaRepository = tipoPessoaRepository;
    }

    public List<Pessoa> buscarTodos() {
        return repository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + id));
    }

    /**
     * Cria uma nova Pessoa, resolvendo a referência de TipoPessoa pelo ID recebido no DTO.
     */
    public Pessoa criar(Pessoa pessoa, Long idTipoPessoa) {
        TipoPessoa tipoPessoa = tipoPessoaRepository.findById(idTipoPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPessoa não encontrado com id: " + idTipoPessoa));
        pessoa.setTipoPessoa(tipoPessoa);
        return repository.save(pessoa);
    }

    public Pessoa atualizar(Long id, Pessoa dados, Long idTipoPessoa) {
        Pessoa existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idPessoa", "createdAt", "tipoPessoa",
                "alunoPerfil", "professorPerfil", "responsavelPerfil");
        if (idTipoPessoa != null) {
            TipoPessoa tipoPessoa = tipoPessoaRepository.findById(idTipoPessoa)
                    .orElseThrow(() -> new ResourceNotFoundException("TipoPessoa não encontrado com id: " + idTipoPessoa));
            existente.setTipoPessoa(tipoPessoa);
        }
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Pessoa existente = buscarPorId(id);
        repository.delete(existente);
    }
}
