package com.diario.de.classe.modules.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaResponsavelRepository extends JpaRepository<PessoaResponsavel, Long> {}
