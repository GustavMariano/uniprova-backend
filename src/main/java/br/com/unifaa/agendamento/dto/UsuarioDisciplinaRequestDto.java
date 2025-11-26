package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class UsuarioDisciplinaRequestDto {
    private Long userId;
    private Long disciplinaId;
    private boolean cursandoDisciplina;
}
