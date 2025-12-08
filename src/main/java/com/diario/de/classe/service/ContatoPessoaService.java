package com.diario.de.classe.service;

import com.diario.de.classe.model.ContatoPessoa;

import java.util.List;

public interface ContatoPessoaService {

    List<ContatoPessoa> buscarTodasPessoasContatos();

    ContatoPessoa buscarContatoPessoaPoridContatoPessoa(Long idContatoPessoa);

    ContatoPessoa criarContatoPessoa(ContatoPessoa contatoPessoa);

    ContatoPessoa atualizarContatoPessoa(ContatoPessoa contatoPessoaDoBanco);

    ContatoPessoa deletarContatoPessoa(ContatoPessoa contatoPessoa);

} 