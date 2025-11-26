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

import br.com.unifaa.agendamento.dto.DisciplinaRequestDto;
import br.com.unifaa.agendamento.dto.DisciplinaResponseDto;
import br.com.unifaa.agendamento.service.DisciplinaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DisciplinaResponseDto create(@RequestBody DisciplinaRequestDto dto) {
        return disciplinaService.create(dto);
    }

    @GetMapping("/{id}")
    public DisciplinaResponseDto get(@PathVariable Long id) {
        return disciplinaService.get(id);
    }

    @GetMapping
    public List<DisciplinaResponseDto> list() {
        return disciplinaService.list();
    }

    @GetMapping("/usuario/{userId}")
    public List<DisciplinaResponseDto> listByUser(@PathVariable Long userId) {
        return disciplinaService.listByUser(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DisciplinaResponseDto update(@PathVariable Long id, @RequestBody DisciplinaRequestDto dto) {
        return disciplinaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        disciplinaService.delete(id);
    }
}
