package com.diario.de.classe.service;

import com.diario.de.classe.model.EnderecoPessoa;
import java.util.List;

public interface EnderecoPessoaService {

    List<EnderecoPessoa> buscarTodosEnderecosPessoas();

    EnderecoPessoa buscarEnderecoPessoaPoridEnderecoPessoa(Long idEnderecoPessoa);

    EnderecoPessoa criarEnderecoPessoa(EnderecoPessoa enderecoPessoa);

    EnderecoPessoa atualizarEnderecoPessoa(EnderecoPessoa enderecoPessoaDoBanco);

    EnderecoPessoa deletarEnderecoPessoa(EnderecoPessoa enderecoPessoa);

} 