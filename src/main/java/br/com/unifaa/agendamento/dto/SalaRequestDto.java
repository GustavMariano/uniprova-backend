package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class SalaRequestDto {

    private Long poloId;
    private String nome;
    private Integer capacidade;
    private String recursos;
    private String status;
}
