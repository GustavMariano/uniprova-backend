package br.com.unifaa.agendamento.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvaluationAvailableSlotsDto {
    private Long evaluationId;
    private String evaluationTitle;
    private List<SlotResponseDto> availableSlots;
}

