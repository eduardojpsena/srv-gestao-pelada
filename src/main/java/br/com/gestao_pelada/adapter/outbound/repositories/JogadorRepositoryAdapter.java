package br.com.gestao_pelada.adapter.outbound.repositories;

import br.com.gestao_pelada.application.port.outbound.JogadorRepositoryPort;

public class JogadorRepositoryAdapter implements JogadorRepositoryPort {

    private final JogadorRepositoryJpa jogadorRepositoryJpa;

    public JogadorRepositoryAdapter(JogadorRepositoryJpa jogadorRepositoryJpa) {
        this.jogadorRepositoryJpa = jogadorRepositoryJpa;
    }

}
