package br.com.unifaa.agendamento.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class MatriculaResponseDto {
    private Long id;
    private Long userId;
    private Long cursoId;
    private Instant dataMatricula;
}