package com.diario.de.classe.repository.jpa;

import com.diario.de.classe.model.old.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositoryJpa extends JpaRepository<Usuario, Long> {
}
