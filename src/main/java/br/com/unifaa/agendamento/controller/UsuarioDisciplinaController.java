package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.UsuarioDisciplinaRequestDto;
import br.com.unifaa.agendamento.dto.UsuarioDisciplinaResponseDto;
import br.com.unifaa.agendamento.service.UsuarioDisciplinaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuario-disciplina")
@RequiredArgsConstructor
public class UsuarioDisciplinaController {

    private final UsuarioDisciplinaService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDisciplinaResponseDto create(@RequestBody UsuarioDisciplinaRequestDto dto) {
        return service.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDisciplinaResponseDto> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
