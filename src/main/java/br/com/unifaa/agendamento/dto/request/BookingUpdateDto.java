package br.com.unifaa.agendamento.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class BookingUpdateDto {
    private UUID bookingId;
    private LocalDateTime newSlotStart;
    private UUID userId;
}
