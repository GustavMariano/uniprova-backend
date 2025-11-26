package br.com.unifaa.agendamento.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvaDetalhadaDto {

    private String titulo;
    private Instant dataAgendada;
    private String sala;
    private String professor;
}
