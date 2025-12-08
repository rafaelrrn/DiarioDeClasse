package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasseRepositoryJpa extends JpaRepository<Classe, Long> {
} 