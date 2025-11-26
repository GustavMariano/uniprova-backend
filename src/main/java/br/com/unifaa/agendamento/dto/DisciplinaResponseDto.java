package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class DisciplinaResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private Long cursoId;
}
