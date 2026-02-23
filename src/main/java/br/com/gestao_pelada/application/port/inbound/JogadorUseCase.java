package br.com.gestao_pelada.application.port.inbound;

import br.com.gestao_pelada.adapter.inbound.models.JogadorRequestDTO;
import br.com.gestao_pelada.domain.model.Jogador;

import java.util.List;

public interface JogadorUseCase {

    Jogador criarJogador(Jogador jogador);

    Jogador buscarJogador(Long id);

    List<Jogador> listarJogadores();

    void deletarJogador(Long id);

}
