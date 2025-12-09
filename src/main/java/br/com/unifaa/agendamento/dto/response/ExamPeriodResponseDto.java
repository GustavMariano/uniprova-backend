package br.com.unifaa.agendamento.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExamPeriodResponseDto {
    private Long id;
    private String code;
    private String title;
    private String startDate;
    private String endDate;
    private String dailyStartTime;
    private String dailyEndTime;
    private int slotDurationMinutes;
    private int slotCapacity;
    private Long campusId;
    private String campusName;
    private Map<String, Integer> slotBookings;
}
