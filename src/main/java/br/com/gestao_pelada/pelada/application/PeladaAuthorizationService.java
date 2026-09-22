package br.com.gestao_pelada.pelada.application;

import br.com.gestao_pelada.evento.infrastructure.EventoRepository;
import br.com.gestao_pelada.partida.infrastructure.PartidaRepository;
import br.com.gestao_pelada.pelada.domain.PapelPelada;
import br.com.gestao_pelada.pelada.domain.PeladaMembro;
import br.com.gestao_pelada.pelada.infrastructure.PeladaMembroRepository;
import br.com.gestao_pelada.shared.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("peladaAuthorization")
@RequiredArgsConstructor
public class PeladaAuthorizationService {

    private final PeladaMembroRepository membroRepository;
    private final PartidaRepository partidaRepository;
    private final EventoRepository eventoRepository;

    public boolean podeAcessar(UUID peladaId, Authentication authentication) {
        return usuarioId(authentication)
                .flatMap(id -> membroRepository.findByPeladaIdAndUsuarioId(peladaId, id))
                .filter(PeladaMembro::isAtivo)
                .isPresent();
    }

    public boolean podeGerenciar(UUID peladaId, Authentication authentication) {
        return usuarioId(authentication)
                .flatMap(id -> membroRepository.findByPeladaIdAndUsuarioId(peladaId, id))
                .filter(PeladaMembro::isAtivo)
                .map(PeladaMembro::getPapel)
                .map(papel -> papel == PapelPelada.ADMIN || papel == PapelPelada.ORGANIZADOR)
                .orElse(false);
    }

    public boolean podeGerenciarPartida(UUID partidaId, Authentication authentication) {
        return partidaRepository.findById(partidaId)
                .map(partida -> podeGerenciar(partida.getPeladaId(), authentication))
                .orElse(false);
    }

    public boolean podeAcessarPartida(UUID partidaId, Authentication authentication) {
        return partidaRepository.findById(partidaId)
                .map(partida -> podeAcessar(partida.getPeladaId(), authentication))
                .orElse(false);
    }

    public boolean podeGerenciarEvento(UUID eventoId, Authentication authentication) {
        return eventoRepository.findById(eventoId)
                .map(evento -> podeGerenciarPartida(evento.getPartidaId(), authentication))
                .orElse(false);
    }

    private java.util.Optional<UUID> usuarioId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return java.util.Optional.of(securityUser.getId());
        }
        return java.util.Optional.empty();
    }
}
