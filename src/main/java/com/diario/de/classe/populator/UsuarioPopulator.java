package com.diario.de.classe.populator;

import com.diario.de.classe.model.old.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class UsuarioPopulator {
    final private static Logger LOG = LogManager.getLogger(UsuarioPopulator.class);

    public Usuario atualizaUsuario(Usuario usuarioRecebido, Usuario usuarioDoBanco){
        if (usuarioRecebido.getNomeCompleto()!=null) usuarioDoBanco.setNomeCompleto(usuarioRecebido.getNomeCompleto());
        if (usuarioRecebido.getCpf()!=null) usuarioDoBanco.setCpf(usuarioRecebido.getCpf());
        if (usuarioRecebido.getEmail()!=null) usuarioDoBanco.setEmail(usuarioRecebido.getEmail());
        if (usuarioRecebido.getSenha()!=null) usuarioDoBanco.setSenha(usuarioRecebido.getSenha());
        if (usuarioRecebido.getPerfil()!=null) usuarioDoBanco.setPerfil(usuarioRecebido.getPerfil());
        if (usuarioRecebido.getDataExpiracaoUltimoToken()!=null) usuarioDoBanco.setDataExpiracaoUltimoToken(usuarioRecebido.getDataExpiracaoUltimoToken());
        if (usuarioRecebido.getQuantidadeErrosDeSenha()!=null) usuarioDoBanco.setQuantidadeErrosDeSenha(usuarioRecebido.getQuantidadeErrosDeSenha());
        if (usuarioRecebido.getAtivo()!=null) usuarioDoBanco.setAtivo(usuarioRecebido.getAtivo());
        if (usuarioRecebido.getVerificado()!=null) usuarioDoBanco.setVerificado(usuarioRecebido.getVerificado());
        if (usuarioRecebido.getCreatedAt()!=null) usuarioDoBanco.setCreatedAt(usuarioRecebido.getCreatedAt());
        if (usuarioRecebido.getUpdatedAt()!=null) usuarioDoBanco.setUpdatedAt(usuarioRecebido.getUpdatedAt());
        return usuarioDoBanco;
    }
}
