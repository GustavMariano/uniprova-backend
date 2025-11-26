package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class UsuarioDisciplinaResponseDto {
    private Long id;
    private Long userId;
    private Long disciplinaId;
    private boolean cursandoDisciplina;
}