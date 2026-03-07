package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoAvaliacaoService {
    private final AlunoAvaliacaoRepository repository;
    private final PessoaRepository pessoaRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public AlunoAvaliacaoService(AlunoAvaliacaoRepository repository, PessoaRepository pessoaRepository,
                                 AvaliacaoRepository avaliacaoRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    public List<AlunoAvaliacao> buscarTodos() { return repository.findAll(); }

    public AlunoAvaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação de aluno não encontrada com id: " + id));
    }

    public AlunoAvaliacao criar(AlunoAvaliacao alunoAvaliacao, Long idAluno, Long idAvaliacao) {
        alunoAvaliacao.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        alunoAvaliacao.setAvaliacao(avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + idAvaliacao)));
        return repository.save(alunoAvaliacao);
    }

    public AlunoAvaliacao atualizar(Long id, AlunoAvaliacao dados, Long idAluno, Long idAvaliacao) {
        AlunoAvaliacao existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idAlunoAvaliacao", "createdAt", "pessoaAluno", "avaliacao");
        if (idAluno != null) existente.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        if (idAvaliacao != null) existente.setAvaliacao(avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + idAvaliacao)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
