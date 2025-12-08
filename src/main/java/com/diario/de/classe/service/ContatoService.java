package com.diario.de.classe.service;

import com.diario.de.classe.model.Contato;

import java.util.List;

public interface ContatoService {

    List<Contato> buscarTodosContatos();

    Contato buscarContatoPoridContato(Long idContato);

    Contato criarContato(Contato contato);

    Contato atualizarContato(Long idContato, Contato contato, Contato contatoDoBanco);

    Contato deletarContato(Contato contato);

} 