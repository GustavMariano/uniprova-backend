package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class ProfessorRequestDto {
    private String nome;
    private String email;
    private String telefone;
    private String status;
    private String departamento;
}
