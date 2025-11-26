package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.SalaRequestDto;
import br.com.unifaa.agendamento.dto.SalaResponseDto;
import br.com.unifaa.agendamento.service.SalaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaResponseDto create(@RequestBody SalaRequestDto dto) {
        return salaService.create(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<SalaResponseDto> findAll() {
        return salaService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public SalaResponseDto findById(@PathVariable Long id) {
        return salaService.findById(id);
    }

    @GetMapping("/usuario/{userId}")
    public List<SalaResponseDto> findByUserPolo(@PathVariable Long userId) {
        return salaService.listByUser(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SalaResponseDto update(@PathVariable Long id, @RequestBody SalaRequestDto dto) {
        return salaService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        salaService.delete(id);
    }
}
