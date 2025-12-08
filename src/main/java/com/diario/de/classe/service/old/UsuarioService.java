package com.diario.de.classe.service.old;

import com.diario.de.classe.model.old.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> buscarTodosUsuarios();

    Usuario buscarUsuarioPorId(Long id);

    Usuario criarUsuario(String usuario);

    Usuario atualizarUsuario(Long id, String usuarioBody, Usuario usuarioDoBanco);

    Usuario deletarUsuario(Usuario usuario);

}
