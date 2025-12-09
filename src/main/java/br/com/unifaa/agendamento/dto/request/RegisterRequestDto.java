package br.com.unifaa.agendamento.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String fullName;
    private String email;
    private String password;
    private Long courseId;
    private Long campusId;
}
