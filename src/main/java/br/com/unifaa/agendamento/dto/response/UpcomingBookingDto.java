package br.com.unifaa.agendamento.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpcomingBookingDto {
    private UUID bookingId;
    private String evaluationTitle;
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
    private String campusName;
}
