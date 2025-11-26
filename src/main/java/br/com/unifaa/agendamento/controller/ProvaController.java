package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.model.Prova;
import br.com.unifaa.agendamento.service.ProvaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/provas")
@RequiredArgsConstructor
public class ProvaController {

    private final ProvaService provaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Prova> create(@RequestBody Prova prova) {
        return ResponseEntity.ok(provaService.create(prova));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Prova>> list() {
        return ResponseEntity.ok(provaService.listAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Prova> get(@PathVariable Long id) {
        return ResponseEntity.ok(provaService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Prova> update(@PathVariable Long id, @RequestBody Prova prova) {
        return ResponseEntity.ok(provaService.update(id, prova));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        provaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
