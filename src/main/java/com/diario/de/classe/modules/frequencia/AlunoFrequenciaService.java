package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.calendario.CalendarioEscolarRepository;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoFrequenciaService {
    private final AlunoFrequenciaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final CalendarioEscolarRepository calendarioEscolarRepository;

    public AlunoFrequenciaService(AlunoFrequenciaRepository repository, PessoaRepository pessoaRepository,
                                  CalendarioEscolarRepository calendarioEscolarRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.calendarioEscolarRepository = calendarioEscolarRepository;
    }

    public List<AlunoFrequencia> buscarTodos() { return repository.findAll(); }

    public AlunoFrequencia buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frequência de aluno não encontrada com id: " + id));
    }

    public AlunoFrequencia criar(AlunoFrequencia alunoFrequencia, Long idAluno, Long idCalendarioEscolar) {
        alunoFrequencia.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        alunoFrequencia.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                .orElseThrow(() -> new ResourceNotFoundException("Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        return repository.save(alunoFrequencia);
    }

    public AlunoFrequencia atualizar(Long id, AlunoFrequencia dados, Long idAluno, Long idCalendarioEscolar) {
        AlunoFrequencia existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idAlunoFrequencia", "createdAt", "pessoaAluno", "calendarioEscolar");
        if (idAluno != null) existente.setPessoaAluno(pessoaRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com id: " + idAluno)));
        if (idCalendarioEscolar != null) existente.setCalendarioEscolar(calendarioEscolarRepository.findById(idCalendarioEscolar)
                .orElseThrow(() -> new ResourceNotFoundException("Calendário escolar não encontrado com id: " + idCalendarioEscolar)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
