package br.com.unifaa.agendamento.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class BookingCreateDto {

    private Long evaluationId;
    private UUID userId;
    private LocalDateTime slotStart; 
}

