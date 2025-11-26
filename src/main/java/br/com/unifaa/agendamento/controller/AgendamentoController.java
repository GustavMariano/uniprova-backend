package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.AgendamentoResponseDto;
import br.com.unifaa.agendamento.dto.HistoricoProvaDto;
import br.com.unifaa.agendamento.dto.ProvaCalendarioDto;
import br.com.unifaa.agendamento.dto.ProvaDetalhadaDto;
import br.com.unifaa.agendamento.model.Agendamento;
import br.com.unifaa.agendamento.service.AgendamentoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> agendar(
            @RequestParam Long userId,
            @RequestParam Long disciplinaId,
            @RequestParam Long poloId,
            @RequestParam String dataHora) {

        return ResponseEntity.ok(service.agendar(userId, disciplinaId, poloId, dataHora));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Agendamento>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/calendario/{userId}")
    public List<ProvaCalendarioDto> provasCalendario(@PathVariable Long userId) {
        return service.listarProvasCalendario(userId);
    }

    @GetMapping("/proximas/{userId}")
    public List<ProvaDetalhadaDto> listarProximas(@PathVariable Long userId) {
        return service.listarProximasProvasDetalhadas(userId);
    }

    @GetMapping("/historico/{userId}")
    public List<HistoricoProvaDto> listarHistorico(@PathVariable Long userId) {
        return service.listarHistoricoProvas(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
