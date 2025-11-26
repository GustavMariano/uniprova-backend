package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class DisciplinaRequestDto {
    private Long cursoId;
    private String nome;
    private String descricao;
}
