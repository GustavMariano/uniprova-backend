package br.com.unifaa.agendamento.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvaCalendarioDto {

    private String title;
    
    private Instant date;
}
