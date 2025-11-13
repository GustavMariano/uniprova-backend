package br.com.unifaa.agendamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequestDto {
    
    @NotBlank private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private Long poloId;
}
