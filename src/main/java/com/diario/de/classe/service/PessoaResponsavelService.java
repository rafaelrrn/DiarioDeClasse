package com.diario.de.classe.service;

import com.diario.de.classe.model.PessoaResponsavel;
import java.util.List;

public interface PessoaResponsavelService {

    List<PessoaResponsavel> buscarTodasPessoasResponsaveis();

    PessoaResponsavel buscarPessoaResponsavelPoridPessoaResponsavel(Long idPessoaResponsavel);

    PessoaResponsavel criarPessoaResponsavel(PessoaResponsavel pessoaResponsavel);

    PessoaResponsavel atualizarPessoaResponsavel(PessoaResponsavel pessoaResponsavelDoBanco);

    PessoaResponsavel deletarPessoaResponsavel(PessoaResponsavel pessoaResponsavel);

} 