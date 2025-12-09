package br.com.unifaa.agendamento.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SlotResponseDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private int remainingCapacity;
}
