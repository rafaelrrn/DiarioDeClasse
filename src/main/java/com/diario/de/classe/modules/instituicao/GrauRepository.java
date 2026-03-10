package com.diario.de.classe.modules.instituicao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrauRepository extends JpaRepository<Grau, Long> {}
