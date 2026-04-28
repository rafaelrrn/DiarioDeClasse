package com.diario.de.classe.modules.cronograma;

import com.diario.de.classe.modules.cronograma.dto.ClasseCronogramaDTO;
import com.diario.de.classe.modules.turma.Classe;
import com.diario.de.classe.modules.turma.ClasseRepository;
import com.diario.de.classe.shared.exception.BusinessException;
import com.diario.de.classe.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClasseCronogramaService {

    private final ClasseCronogramaRepository repository;
    private final ClasseRepository classeRepository;
    private final CronogramaAnualRepository cronogramaAnualRepository;

    public ClasseCronogramaService(ClasseCronogramaRepository repository,
                                   ClasseRepository classeRepository,
                                   CronogramaAnualRepository cronogramaAnualRepository) {
        this.repository = repository;
        this.classeRepository = classeRepository;
        this.cronogramaAnualRepository = cronogramaAnualRepository;
    }

    public List<ClasseCronogramaDTO> buscarTodos() {
        return repository.findAll().stream().map(ClasseCronogramaDTO::new).toList();
    }

    public ClasseCronogramaDTO buscarPorId(Long id) {
        return new ClasseCronogramaDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vínculo classe-cronograma não encontrado com id: " + id)));
    }

    public List<ClasseCronogramaDTO> buscarPorClasse(Long idClasse) {
        return repository.findByClasse_IdClasse(idClasse)
                .stream().map(ClasseCronogramaDTO::new).toList();
    }

    public List<ClasseCronogramaDTO> buscarPorCronograma(Long idCronograma) {
        return repository.findByCronogramaAnual_IdCronogramaAnual(idCronograma)
                .stream().map(ClasseCronogramaDTO::new).toList();
    }

    @Transactional
    public ClasseCronogramaDTO vincular(Long idClasse, Long idCronograma) {
        Classe classe = classeRepository.findById(idClasse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Classe não encontrada com id: " + idClasse));

        CronogramaAnual cronograma = cronogramaAnualRepository.findByIdCronogramaAnualAndAtivoTrue(idCronograma)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cronograma anual não encontrado com id: " + idCronograma));

        if (repository.existsByClasse_IdClasseAndCronogramaAnual_IdCronogramaAnual(idClasse, idCronograma)) {
            throw new BusinessException(
                    "Esta classe já está vinculada a este cronograma anual.");
        }

        ClasseCronograma vínculo = new ClasseCronograma();
        vínculo.setClasse(classe);
        vínculo.setCronogramaAnual(cronograma);

        return new ClasseCronogramaDTO(repository.save(vínculo));
    }

    @Transactional
    public void desvincular(Long id) {
        ClasseCronograma existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vínculo classe-cronograma não encontrado com id: " + id));
        existente.setAtivo(false);
        existente.setDeletedAt(LocalDateTime.now());
        repository.save(existente);
    }
}
