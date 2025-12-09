package br.com.unifaa.agendamento.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponseDto {
    private String email;
}
