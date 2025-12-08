package com.diario.de.classe.service;

import com.diario.de.classe.model.TipoPessoa;
import java.util.List;

public interface TipoPessoaService {

    List<TipoPessoa> buscarTodosTiposPessoas();

    TipoPessoa buscarTipoPessoaPoridTipoPessoa(Long idTipoPessoa);

    TipoPessoa criarTipoPessoa(TipoPessoa tipoPessoa);

    TipoPessoa atualizarTipoPessoa(Long idTipoPessoa, TipoPessoa tipoPessoa, TipoPessoa tipoPessoaDoBanco);

    TipoPessoa deletarTipoPessoa(TipoPessoa tipoPessoa);

} 