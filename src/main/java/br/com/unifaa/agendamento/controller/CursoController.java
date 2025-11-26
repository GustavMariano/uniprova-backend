package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.CursoRequestDto;
import br.com.unifaa.agendamento.dto.CursoResponseDto;
import br.com.unifaa.agendamento.service.CursoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CursoResponseDto create(@RequestBody CursoRequestDto dto) {
        return cursoService.create(dto);
    }

    @GetMapping("/{id}")
    public CursoResponseDto get(@PathVariable Long id) {
        return cursoService.get(id);
    }

    @GetMapping
    public List<CursoResponseDto> list() {
        return cursoService.list();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CursoResponseDto update(@PathVariable Long id, @RequestBody CursoRequestDto dto) {
        return cursoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        cursoService.delete(id);
    }
}

