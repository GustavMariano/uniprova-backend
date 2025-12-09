package br.com.unifaa.agendamento.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActiveEvaluationResponseDto {
    private String code;
    private String name;
    private String period;
    private String timeRange;
    private String campus;
    private boolean status;
}
