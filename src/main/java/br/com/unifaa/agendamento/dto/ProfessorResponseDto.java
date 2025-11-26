package br.com.unifaa.agendamento.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfessorResponseDto {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String status;
    private String departamento;
    private Instant criadoEm;
}
