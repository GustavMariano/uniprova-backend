package br.com.unifaa.agendamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    
    @NotBlank private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private Long poloId;
}
