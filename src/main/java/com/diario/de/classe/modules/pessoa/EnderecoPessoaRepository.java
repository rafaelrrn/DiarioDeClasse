package com.diario.de.classe.modules.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoPessoaRepository extends JpaRepository<EnderecoPessoa, Long> {}
