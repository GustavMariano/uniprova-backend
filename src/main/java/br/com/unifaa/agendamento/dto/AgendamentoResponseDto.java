package br.com.unifaa.agendamento.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendamentoResponseDto {

    private Long id;
    private Long userId;
    private Long provaId;
    private Long poloId;
    private String status;
    private Instant dataAgendamento;
}