package br.com.unifaa.agendamento.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluationCreateDto {

    @NotBlank
    private String code;

    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalTime dailyStartTime;

    @NotNull
    private LocalTime dailyEndTime;

    @NotNull
    @Min(1)
    private Integer slotDurationMinutes;

    @NotNull
    @Min(1)
    private Integer slotCapacity;

    @NotNull
    private Long campusId;
}

