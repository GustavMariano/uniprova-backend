package br.com.unifaa.agendamento.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class CursoResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private Instant criadoEm;
}
