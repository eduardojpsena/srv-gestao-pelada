package br.com.gestao_pelada.pelada.service;

import br.com.gestao_pelada.evento.repository.EventoRepository;
import br.com.gestao_pelada.partida.repository.PartidaRepository;
import br.com.gestao_pelada.pelada.model.enums.PapelPelada;
import br.com.gestao_pelada.pelada.model.entity.PeladaMembro;
import br.com.gestao_pelada.pelada.repository.PeladaMembroRepository;
import br.com.gestao_pelada.shared.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component("peladaAuthorization")
@RequiredArgsConstructor
public class PeladaAuthorizationService {

    private final PeladaMembroRepository membroRepository;
    private final PartidaRepository partidaRepository;
    private final EventoRepository eventoRepository;

    public boolean podeAcessar(Long peladaId, Authentication authentication) {
        return usuarioId(authentication)
                .flatMap(id -> membroRepository.findByPeladaIdAndUsuarioId(peladaId, id))
                .filter(PeladaMembro::isAtivo)
                .isPresent();
    }

    public boolean podeGerenciar(Long peladaId, Authentication authentication) {
        return usuarioId(authentication)
                .flatMap(id -> membroRepository.findByPeladaIdAndUsuarioId(peladaId, id))
                .filter(PeladaMembro::isAtivo)
                .map(PeladaMembro::getPapel)
                .map(papel -> papel == PapelPelada.ADMIN || papel == PapelPelada.ORGANIZADOR)
                .orElse(false);
    }

    public boolean podeGerenciarPartida(Long partidaId, Authentication authentication) {
        return partidaRepository.findById(partidaId)
                .map(partida -> podeGerenciar(partida.getPeladaId(), authentication))
                .orElse(false);
    }

    public boolean podeAcessarPartida(Long partidaId, Authentication authentication) {
        return partidaRepository.findById(partidaId)
                .map(partida -> podeAcessar(partida.getPeladaId(), authentication))
                .orElse(false);
    }

    public boolean podeGerenciarEvento(Long eventoId, Authentication authentication) {
        return eventoRepository.findById(eventoId)
                .map(evento -> podeGerenciarPartida(evento.getPartidaId(), authentication))
                .orElse(false);
    }

    private java.util.Optional<Long> usuarioId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return java.util.Optional.of(securityUser.getId());
        }
        return java.util.Optional.empty();
    }
}

