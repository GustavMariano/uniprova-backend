package br.com.unifaa.agendamento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import br.com.unifaa.agendamento.dto.request.AuthRequestDto;
import br.com.unifaa.agendamento.dto.request.RegisterRequestDto;
import br.com.unifaa.agendamento.dto.response.AuthResponseDto;
import br.com.unifaa.agendamento.dto.response.RegisterResponseDto;
import br.com.unifaa.agendamento.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto req) {
        return authService.login(req);
    }
}

