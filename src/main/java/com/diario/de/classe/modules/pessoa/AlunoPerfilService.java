package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoPerfilService {

    private final AlunoPerfilRepository repository;
    private final PessoaRepository pessoaRepository;

    public AlunoPerfilService(AlunoPerfilRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<AlunoPerfil> buscarTodos() {
        return repository.findAll();
    }

    public AlunoPerfil buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AlunoPerfil não encontrado com id: " + id));
    }

    public AlunoPerfil buscarPorPessoa(Long idPessoa) {
        return repository.findByPessoa_IdPessoa(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("AlunoPerfil não encontrado para a Pessoa com id: " + idPessoa));
    }

    /**
     * Cria o perfil de aluno para uma Pessoa.
     * Valida que a Pessoa existe, que ainda não possui perfil de aluno
     * e que a matrícula não está duplicada.
     */
    public AlunoPerfil criar(Long idPessoa, AlunoPerfil dados) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com id: " + idPessoa));

        if (repository.existsByPessoa_IdPessoa(idPessoa)) {
            throw new BusinessException("Esta Pessoa já possui um perfil de aluno cadastrado.");
        }

        if (repository.existsByMatricula(dados.getMatricula())) {
            throw new BusinessException("Já existe um aluno com a matrícula: " + dados.getMatricula());
        }

        dados.setPessoa(pessoa);
        return repository.save(dados);
    }

    /**
     * Atualiza apenas os campos editáveis do perfil.
     * idPessoa não pode ser alterado após a criação.
     */
    public AlunoPerfil atualizar(Long id, AlunoPerfil dados) {
        AlunoPerfil existente = buscarPorId(id);

        if (!existente.getMatricula().equals(dados.getMatricula())
                && repository.existsByMatricula(dados.getMatricula())) {
            throw new BusinessException("Já existe um aluno com a matrícula: " + dados.getMatricula());
        }

        existente.setMatricula(dados.getMatricula());
        existente.setDataMatricula(dados.getDataMatricula());
        existente.setNecessidadeEspecial(dados.getNecessidadeEspecial());
        existente.setDescricaoNee(dados.getDescricaoNee());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        repository.delete(buscarPorId(id));
    }
}
