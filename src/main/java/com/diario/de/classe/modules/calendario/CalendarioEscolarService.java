package com.diario.de.classe.modules.calendario;

import com.diario.de.classe.modules.turma.ClasseRepository;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarioEscolarService {
    private final CalendarioEscolarRepository repository;
    private final MesRepository mesRepository;
    private final AnoCalendarioRepository anoCalendarioRepository;
    private final PeriodoRepository periodoRepository;
    private final ClasseRepository classeRepository;

    public CalendarioEscolarService(CalendarioEscolarRepository repository, MesRepository mesRepository,
                                    AnoCalendarioRepository anoCalendarioRepository,
                                    PeriodoRepository periodoRepository, ClasseRepository classeRepository) {
        this.repository = repository;
        this.mesRepository = mesRepository;
        this.anoCalendarioRepository = anoCalendarioRepository;
        this.periodoRepository = periodoRepository;
        this.classeRepository = classeRepository;
    }

    public List<CalendarioEscolar> buscarTodos() { return repository.findAll(); }

    public CalendarioEscolar buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendário escolar não encontrado com id: " + id));
    }

    public CalendarioEscolar criar(CalendarioEscolar calendarioEscolar, Long idMes, Long idAnoCalendario,
                                   Long idPeriodo, Long idClasse) {
        calendarioEscolar.setMes(mesRepository.findById(idMes)
                .orElseThrow(() -> new ResourceNotFoundException("Mês não encontrado com id: " + idMes)));
        if (idAnoCalendario != null) {
            calendarioEscolar.setAnoCalendario(anoCalendarioRepository.findById(idAnoCalendario)
                    .orElseThrow(() -> new ResourceNotFoundException("Ano calendário não encontrado com id: " + idAnoCalendario)));
        }
        calendarioEscolar.setPeriodo(periodoRepository.findById(idPeriodo)
                .orElseThrow(() -> new ResourceNotFoundException("Período não encontrado com id: " + idPeriodo)));
        calendarioEscolar.setClasse(classeRepository.findById(idClasse)
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com id: " + idClasse)));
        return repository.save(calendarioEscolar);
    }

    public CalendarioEscolar atualizar(Long id, CalendarioEscolar dados, Long idMes, Long idAnoCalendario,
                                       Long idPeriodo, Long idClasse) {
        CalendarioEscolar existente = buscarPorId(id);
        BeanUtils.copyProperties(dados, existente, "idCalendarioEscolar", "createdAt", "mes", "anoCalendario", "periodo", "classe");
        if (idMes != null) existente.setMes(mesRepository.findById(idMes)
                .orElseThrow(() -> new ResourceNotFoundException("Mês não encontrado com id: " + idMes)));
        if (idAnoCalendario != null) existente.setAnoCalendario(anoCalendarioRepository.findById(idAnoCalendario)
                .orElseThrow(() -> new ResourceNotFoundException("Ano calendário não encontrado com id: " + idAnoCalendario)));
        if (idPeriodo != null) existente.setPeriodo(periodoRepository.findById(idPeriodo)
                .orElseThrow(() -> new ResourceNotFoundException("Período não encontrado com id: " + idPeriodo)));
        if (idClasse != null) existente.setClasse(classeRepository.findById(idClasse)
                .orElseThrow(() -> new ResourceNotFoundException("Classe não encontrada com id: " + idClasse)));
        return repository.save(existente);
    }

    public void deletar(Long id) { repository.delete(buscarPorId(id)); }
}
