package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.CronogramaAnualDTO;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CronogramaAnualService {

    private final CronogramaAnualRepository repository;

    public CronogramaAnualService(CronogramaAnualRepository repository) {
        this.repository = repository;
    }

    public List<CronogramaAnualDTO> buscarTodos() {
        return repository.findAllByAtivoTrue().stream().map(CronogramaAnualDTO::new).toList();
    }

    public CronogramaAnualDTO buscarPorId(Long id) {
        return new CronogramaAnualDTO(repository.findByIdCronogramaAnualAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cronograma anual não encontrado com id: " + id)));
    }

    public List<CronogramaAnualDTO> buscarPorStatus(String status) {
        return repository.findAllByAtivoTrue().stream()
                .filter(c -> status.equalsIgnoreCase(c.getStatus()))
                .map(CronogramaAnualDTO::new)
                .toList();
    }

    @Transactional
    public CronogramaAnualDTO criar(CronogramaAnual dados) {
        if ("ATIVO".equalsIgnoreCase(dados.getStatus())) {
            boolean jaExisteAtivo = repository.findAllByAtivoTrue().stream()
                    .anyMatch(c -> c.getAno().equals(dados.getAno()) && "ATIVO".equalsIgnoreCase(c.getStatus()));
            if (jaExisteAtivo) {
                throw new BusinessException(
                        "Já existe um cronograma ATIVO para o ano " + dados.getAno() + ".");
            }
        }
        return new CronogramaAnualDTO(repository.save(dados));
    }

    @Transactional
    public CronogramaAnualDTO atualizar(Long id, CronogramaAnual dados) {
        CronogramaAnual existente = repository.findByIdCronogramaAnualAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cronograma anual não encontrado com id: " + id));

        if ("ATIVO".equalsIgnoreCase(dados.getStatus())
                && !existente.getAno().equals(dados.getAno())) {
            boolean jaExisteAtivo = repository.findAllByAtivoTrue().stream()
                    .anyMatch(c -> !c.getIdCronogramaAnual().equals(id)
                            && c.getAno().equals(dados.getAno())
                            && "ATIVO".equalsIgnoreCase(c.getStatus()));
            if (jaExisteAtivo) {
                throw new BusinessException(
                        "Já existe um cronograma ATIVO para o ano " + dados.getAno() + ".");
            }
        }

        existente.setAno(dados.getAno());
        existente.setDataInicio(dados.getDataInicio());
        existente.setDataFim(dados.getDataFim());
        existente.setDiasLetivosPrevistos(dados.getDiasLetivosPrevistos());
        existente.setCargaHorariaPrevista(dados.getCargaHorariaPrevista());
        existente.setStatus(dados.getStatus());

        return new CronogramaAnualDTO(repository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        CronogramaAnual existente = repository.findByIdCronogramaAnualAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cronograma anual não encontrado com id: " + id));
        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
