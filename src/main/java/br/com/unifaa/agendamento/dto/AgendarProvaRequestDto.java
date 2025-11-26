package br.com.unifaa.agendamento.dto;

import lombok.Data;

@Data
public class AgendarProvaRequestDto {
    
    private Long userId;
    private Long disciplinaId;
    private Long salaId;
    private String data;  
    private String horario; 
    private Long poloId;
}
