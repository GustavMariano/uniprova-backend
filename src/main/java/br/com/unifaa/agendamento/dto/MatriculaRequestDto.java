package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class MatriculaRequestDto {
    private Long userId;
    private Long cursoId;
}