package com.diario.de.classe.service;

import com.diario.de.classe.model.Pessoa;
import java.util.List;

public interface PessoaService {

    List<Pessoa> buscarTodasPessoas();

    Pessoa buscarPessoaPoridPessoa(Long idPessoa);

    Pessoa criarPessoa(Pessoa pessoa);

    Pessoa atualizarPessoa(Pessoa pessoaDoBanco);

    Pessoa deletarPessoa(Pessoa pessoa);

} 