package br.com.unifaa.agendamento.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalaResponseDto {

    private Long id;
    private Long poloId;
    private String nome;
    private Integer capacidade;
    private String recursos;
    private String status;
}
