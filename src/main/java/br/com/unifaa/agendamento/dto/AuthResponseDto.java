package br.com.unifaa.agendamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    
    private String token;
    
    private String role;
}
