package com.diario.de.classe.modules.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoPerfilRepository extends JpaRepository<AlunoPerfil, Long> {

    Optional<AlunoPerfil> findByPessoa_IdPessoa(Long idPessoa);

    boolean existsByPessoa_IdPessoa(Long idPessoa);

    boolean existsByMatricula(String matricula);
}
