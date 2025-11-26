package br.com.unifaa.agendamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String token;

    private String role;

    private Long id;

    private String nome;

    private String email;

    private Long poloId;

    private String curso;
}
