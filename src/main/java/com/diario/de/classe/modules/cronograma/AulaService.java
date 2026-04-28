package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.AulaDTO;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.modules.pessoa.PessoaRepository;
import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.modules.turma.ClasseRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AulaService {

    private final AulaRepository repository;
    private final ClasseRepository classeRepository;
    private final PeriodoLetivoRepository periodoLetivoRepository;
    private final PessoaRepository pessoaRepository;

    public AulaService(AulaRepository repository,
                       ClasseRepository classeRepository,
                       PeriodoLetivoRepository periodoLetivoRepository,
                       PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.classeRepository = classeRepository;
        this.periodoLetivoRepository = periodoLetivoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<AulaDTO> buscarTodos() {
        return repository.findAll().stream().map(AulaDTO::new).toList();
    }

    public AulaDTO buscarPorId(Long id) {
        return new AulaDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + id)));
    }

    public List<AulaDTO> buscarPorClasseEData(Long idClasse, LocalDate data) {
        return repository.findByClasse_IdClasseAndDataAula(idClasse, data)
                .stream().map(AulaDTO::new).toList();
    }

    public List<AulaDTO> buscarPorPeriodoEClasse(Long idPeriodo, Long idClasse) {
        return repository.findByPeriodoLetivo_IdPeriodoLetivoAndClasse_IdClasse(idPeriodo, idClasse)
                .stream().map(AulaDTO::new).toList();
    }

    @Transactional
    public AulaDTO criar(Aula dados, Long idClasse, Long idPeriodo, Long idRegistradoPor) {
        Classe classe = classeRepository.findById(idClasse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classe não encontrada com id: " + idClasse));

        PeriodoLetivo periodo = periodoLetivoRepository.findById(idPeriodo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Período letivo não encontrado com id: " + idPeriodo));

        Pessoa registradoPor = pessoaRepository.findById(idRegistradoPor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pessoa não encontrada com id: " + idRegistradoPor));

        if (repository.existsByClasse_IdClasseAndDataAulaAndNumeroAula(
                idClasse, dados.getDataAula(), dados.getNumeroAula())) {
            throw new BusinessException(
                    "Já existe uma aula registrada para esta classe na data "
                    + dados.getDataAula() + ", número " + dados.getNumeroAula() + ".");
        }

        LocalDate dataAula = dados.getDataAula();
        if (dataAula.isBefore(periodo.getDataInicio()) || dataAula.isAfter(periodo.getDataFim())) {
            throw new BusinessException(
                    "A data da aula deve estar dentro do período letivo ("
                    + periodo.getDataInicio() + " — " + periodo.getDataFim() + ").");
        }

        dados.setClasse(classe);
        dados.setPeriodoLetivo(periodo);
        dados.setRegistradoPor(registradoPor);

        return new AulaDTO(repository.save(dados));
    }

    @Transactional
    public AulaDTO encerrarChamada(Long idAula) {
        Aula aula = repository.findById(idAula)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + idAula));

        if (Boolean.TRUE.equals(aula.getChamadaEncerrada())) {
            throw new BusinessException("A chamada desta aula já foi encerrada.");
        }

        aula.setChamadaEncerrada(true);
        return new AulaDTO(repository.save(aula));
    }

    @Transactional
    public AulaDTO atualizar(Long id, Aula dados) {
        Aula existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + id));

        if (Boolean.TRUE.equals(existente.getChamadaEncerrada())) {
            throw new BusinessException(
                    "Não é possível alterar uma aula com chamada já encerrada.");
        }

        existente.setDataAula(dados.getDataAula());
        existente.setNumeroAula(dados.getNumeroAula());
        existente.setConteudoMinistrado(dados.getConteudoMinistrado());

        return new AulaDTO(repository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        Aula existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada com id: " + id));

        if (Boolean.TRUE.equals(existente.getChamadaEncerrada())) {
            throw new BusinessException(
                    "Não é possível excluir uma aula com chamada já encerrada.");
        }

        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
