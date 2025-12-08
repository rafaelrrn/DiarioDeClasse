package com.diario.de.classe.service;

import com.diario.de.classe.model.Endereco;
import java.util.List;

public interface EnderecoService {

    List<Endereco> buscarTodosEnderecos();

    Endereco buscarEnderecoPoridEndereco(Long idEndereco);

    Endereco criarEndereco(Endereco endereco);

    Endereco atualizarEndereco(Long idEndereco, Endereco endereco, Endereco enderecoDoBanco);

    Endereco deletarEndereco(Endereco endereco);

} 