package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorPerfilService {

    private final ProfessorPerfilRepository repository;
    private final PessoaRepository pessoaRepository;

    public ProfessorPerfilService(ProfessorPerfilRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<ProfessorPerfil> buscarTodos() {
        return repository.findAll();
    }

    public ProfessorPerfil buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfessorPerfil não encontrado com id: " + id));
    }

    public ProfessorPerfil buscarPorPessoa(Long idPessoa) {
        return repository.findByPessoa_IdPessoa(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("ProfessorPerfil não encontrado para a Pessoa com id: " + idPessoa));
    }

    /**
     * Cria o perfil de professor para uma Pessoa.
     * Valida que a Pessoa existe e que ainda não possui perfil de professor.
     */
    public ProfessorPerfil criar(Long idPessoa, ProfessorPerfil dados) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + idPessoa));

        if (repository.existsByPessoa_IdPessoa(idPessoa)) {
            throw new BusinessException("Esta Pessoa já possui um perfil de professor cadastrado.");
        }

        dados.setPessoa(pessoa);
        return repository.save(dados);
    }

    /**
     * Atualiza apenas os campos editáveis do perfil.
     * idPessoa não pode ser alterado após a criação.
     */
    public ProfessorPerfil atualizar(Long id, ProfessorPerfil dados) {
        ProfessorPerfil existente = buscarPorId(id);

        existente.setRegistroMec(dados.getRegistroMec());
        existente.setFormacao(dados.getFormacao());
        existente.setDataAdmissao(dados.getDataAdmissao());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        repository.delete(buscarPorId(id));
    }
}
