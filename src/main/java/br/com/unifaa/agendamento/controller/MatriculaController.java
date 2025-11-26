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

import br.com.unifaa.agendamento.dto.MatriculaRequestDto;
import br.com.unifaa.agendamento.dto.MatriculaResponseDto;
import br.com.unifaa.agendamento.service.MatriculaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MatriculaResponseDto create(@RequestBody MatriculaRequestDto dto) {
        return matriculaService.create(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MatriculaResponseDto> list() {
        return matriculaService.list();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        matriculaService.delete(id);
    }
}

