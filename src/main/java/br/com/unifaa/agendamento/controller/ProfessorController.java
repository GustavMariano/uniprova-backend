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

import br.com.unifaa.agendamento.dto.ProfessorRequestDto;
import br.com.unifaa.agendamento.dto.ProfessorResponseDto;
import br.com.unifaa.agendamento.service.ProfessorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProfessorResponseDto create(@RequestBody ProfessorRequestDto dto) {
        return professorService.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ProfessorResponseDto> list() {
        return professorService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ProfessorResponseDto get(@PathVariable Long id) {
        return professorService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProfessorResponseDto update(@PathVariable Long id, @RequestBody ProfessorRequestDto dto) {
        return professorService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        professorService.delete(id);
    }
}
