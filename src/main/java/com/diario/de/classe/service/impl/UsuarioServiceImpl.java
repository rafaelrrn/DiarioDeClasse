package com.diario.de.classe.service.impl;

import com.diario.de.classe.model.old.Usuario;
import com.diario.de.classe.populator.UsuarioPopulator;
import com.diario.de.classe.repository.jpa.UsuarioRepositoryJpa;
import com.diario.de.classe.service.old.UsuarioService;
import com.diario.de.classe.util.ConversorObjetoEntidadeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    final private static Logger LOG = LogManager.getLogger(UsuarioServiceImpl.class);

    @Autowired
    UsuarioRepositoryJpa usuarioRepositoryJpa;
    @Autowired
    ConversorObjetoEntidadeUtil conversorObjetoEntidadeUtil;
    @Autowired
    UsuarioPopulator usuarioPopulator;

    @Override
    public List<Usuario> buscarTodosUsuarios(){
        List<Usuario> usuarioList = usuarioRepositoryJpa.findAll();
        return usuarioList;
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id){
        Optional<Usuario> usuario = usuarioRepositoryJpa.findById(id);
        if (usuario.isPresent()) return usuario.get();
        return null;
    }

    @Override
    public Usuario criarUsuario(String aUsuario){
        Usuario usuario = conversorObjetoEntidadeUtil.converterObjetoEmEntidade(aUsuario, Usuario.class);
        if (!ObjectUtils.isEmpty(usuario)) usuarioRepositoryJpa.save(usuario);
        return usuario;
    }

    @Override
    public Usuario atualizarUsuario(Long id, String usuarioBody, Usuario usuarioDoBanco){
        Usuario usuarioRecebido = conversorObjetoEntidadeUtil.converterObjetoEmEntidade(usuarioBody, Usuario.class);
        usuarioDoBanco = usuarioPopulator.atualizaUsuario(usuarioRecebido, usuarioDoBanco);
        return usuarioRepositoryJpa.save(usuarioDoBanco);
    }

    @Override
    public Usuario deletarUsuario(Usuario usuario){
        usuarioRepositoryJpa.delete(usuario);
        return usuario;
    }

}
