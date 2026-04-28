package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.PeriodoLetivoDTO;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PeriodoLetivoService {

    private final PeriodoLetivoRepository repository;
    private final CronogramaAnualRepository cronogramaAnualRepository;

    public PeriodoLetivoService(PeriodoLetivoRepository repository,
                                CronogramaAnualRepository cronogramaAnualRepository) {
        this.repository = repository;
        this.cronogramaAnualRepository = cronogramaAnualRepository;
    }

    public List<PeriodoLetivoDTO> buscarTodos() {
        return repository.findAll().stream().map(PeriodoLetivoDTO::new).toList();
    }

    public PeriodoLetivoDTO buscarPorId(Long id) {
        return new PeriodoLetivoDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Período letivo não encontrado com id: " + id)));
    }

    public List<PeriodoLetivoDTO> buscarPorCronograma(Long idCronograma) {
        return repository.findByCronogramaAnual_IdCronogramaAnualOrderByOrdem(idCronograma)
                .stream().map(PeriodoLetivoDTO::new).toList();
    }

    @Transactional
    public PeriodoLetivoDTO criar(PeriodoLetivo dados, Long idCronograma) {
        CronogramaAnual cronograma = cronogramaAnualRepository.findByIdCronogramaAnualAndAtivoTrue(idCronograma)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cronograma anual não encontrado com id: " + idCronograma));

        if (repository.existsByCronogramaAnual_IdCronogramaAnualAndOrdem(idCronograma, dados.getOrdem())) {
            throw new BusinessException(
                    "Já existe um período com ordem " + dados.getOrdem()
                    + " neste cronograma.");
        }

        if (dados.getDataInicio().isBefore(cronograma.getDataInicio())
                || dados.getDataFim().isAfter(cronograma.getDataFim())) {
            throw new BusinessException(
                    "As datas do período devem estar dentro do intervalo do cronograma ("
                    + cronograma.getDataInicio() + " — " + cronograma.getDataFim() + ").");
        }

        dados.setCronogramaAnual(cronograma);
        return new PeriodoLetivoDTO(repository.save(dados));
    }

    @Transactional
    public PeriodoLetivoDTO atualizar(Long id, PeriodoLetivo dados) {
        PeriodoLetivo existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Período letivo não encontrado com id: " + id));

        existente.setNome(dados.getNome());
        existente.setTipo(dados.getTipo());
        existente.setDataInicio(dados.getDataInicio());
        existente.setDataFim(dados.getDataFim());
        existente.setOrdem(dados.getOrdem());

        return new PeriodoLetivoDTO(repository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        PeriodoLetivo existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Período letivo não encontrado com id: " + id));
        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
