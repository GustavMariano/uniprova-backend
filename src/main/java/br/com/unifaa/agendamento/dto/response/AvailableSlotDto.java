package br.com.unifaa.agendamento.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableSlotDto {
    private LocalDateTime slotStart;
    private LocalDateTime slotEnd;
    private int remainingCapacity;
}

