package br.com.unifaa.agendamento.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private UUID id;
    private String token;
    private String fullName;
    private String email;
    private String course;
    private Long campusId;
}
