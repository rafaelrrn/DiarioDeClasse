package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoTurmaService {
    private final AlunoTurmaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final TurmaRepository turmaRepository;

    public AlunoTurmaService(AlunoTurmaRepository repository, PessoaRepository pessoaRepository,
                             TurmaRepository turmaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.turmaRepository = turmaRepository;
    }

    public List<AlunoTurma> buscarTodos() { return repository.findAll(); }

    public AlunoTurma buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AlunoTurma não encontrado com id: " + id));
    }

    public AlunoTurma criar(AlunoTurma alunoTurma, Long idAluno, Long idTurma) {
        alunoTurma.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        alunoTurma.setTurma(turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + idTurma)));
        return repository.save(alunoTurma);
    }

    public AlunoTurma atualizar(Long id, AlunoTurma dados, Long idAluno, Long idTurma) {
        AlunoTurma existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idAlunoTurma", "createdAt", "pessoaAluno", "turma");
        if (idAluno != null) existente.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        if (idTurma != null) existente.setTurma(turmaRepository.findById(idTurma)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com id: " + idTurma)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
