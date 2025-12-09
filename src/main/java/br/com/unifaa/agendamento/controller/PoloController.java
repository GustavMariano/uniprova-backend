package br.com.unifaa.agendamento.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.response.ExamPeriodResponseDto;
import br.com.unifaa.agendamento.service.BookingReportPdfService;
import br.com.unifaa.agendamento.service.EvaluationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/polo")
@RequiredArgsConstructor
public class PoloController {
    
    
    private final EvaluationService evaluationService;
    private final BookingReportPdfService bookingReportPdfService;

    @GetMapping
    @PreAuthorize("hasRole('POLO')")
    public ResponseEntity<List<ExamPeriodResponseDto>> getExamPeriods() {
        return ResponseEntity.ok(evaluationService.listarPeriodosDeProva());
    }

    @GetMapping("report/{id}/pdf")
    @PreAuthorize("hasRole('POLO')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws IOException {

        byte[] pdf = bookingReportPdfService.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio_presenca_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
