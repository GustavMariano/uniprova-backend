package br.com.unifaa.agendamento.controller;

import br.com.unifaa.agendamento.dto.request.EvaluationCreateDto;
import br.com.unifaa.agendamento.dto.response.ActiveEvaluationResponseDto;
import br.com.unifaa.agendamento.dto.response.SlotResponseDto;
import br.com.unifaa.agendamento.model.Evaluation;
import br.com.unifaa.agendamento.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EvaluationService evaluationService;

    @PostMapping("/evaluations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evaluation> createEvaluation(@Valid @RequestBody EvaluationCreateDto dto) {
        Evaluation created = evaluationService.createEvaluation(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/evaluations/{id}/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SlotResponseDto>> getEvaluationSlots(@PathVariable("id") Long id) {
        List<SlotResponseDto> slots = evaluationService.generateSlotsForEvaluation(id);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/evaluations/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActiveEvaluationResponseDto>> getActiveEvaluations() {
        return ResponseEntity.ok(evaluationService.listActiveEvaluations());
    }

}
