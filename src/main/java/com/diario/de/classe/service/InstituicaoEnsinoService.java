package com.diario.de.classe.service;

import com.diario.de.classe.model.InstituicaoEnsino;
import java.util.List;

public interface InstituicaoEnsinoService {

    List<InstituicaoEnsino> buscarTodasInstituicoesEnsino();

    InstituicaoEnsino buscarInstituicaoEnsinoPoridInstituicaoEnsino(Long idInstituicaoEnsino);

    InstituicaoEnsino criarInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino);

    InstituicaoEnsino atualizarInstituicaoEnsino(Long idInstituicaoEnsino, InstituicaoEnsino instituicaoEnsino, InstituicaoEnsino instituicaoEnsinoDoBanco);

    InstituicaoEnsino deletarInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino);

} 