package br.com.gestao_pelada.adapter.inbound.controller;

import br.com.gestao_pelada.application.port.inbound.JogadorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorUseCase jogadorUseCase;

}
