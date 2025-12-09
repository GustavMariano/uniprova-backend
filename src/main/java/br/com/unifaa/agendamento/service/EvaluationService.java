package br.com.unifaa.agendamento.service;

import br.com.unifaa.agendamento.dto.request.EvaluationCreateDto;
import br.com.unifaa.agendamento.dto.response.ActiveEvaluationResponseDto;
import br.com.unifaa.agendamento.dto.response.ExamPeriodResponseDto;
import br.com.unifaa.agendamento.dto.response.SlotResponseDto;
import br.com.unifaa.agendamento.enums.BookingStatus;
import br.com.unifaa.agendamento.model.Campus;
import br.com.unifaa.agendamento.model.Evaluation;
import br.com.unifaa.agendamento.repository.CampusRepository;
import br.com.unifaa.agendamento.repository.EvaluationRepository;
import br.com.unifaa.agendamento.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final CampusRepository campusRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Evaluation createEvaluation(EvaluationCreateDto dto) {

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new RuntimeException("endDate deve ser maior ou igual a startDate");
        }

        if (dto.getDailyEndTime().isBefore(dto.getDailyStartTime())) {
            throw new RuntimeException("dailyEndTime deve ser depois de dailyStartTime");
        }

        if (dto.getSlotDurationMinutes() <= 0) {
            throw new RuntimeException("slotDurationMinutes deve ser positivo");
        }

        Campus campus = campusRepository.findById(dto.getCampusId())
                .orElseThrow(() -> new RuntimeException("Polo não encontrado: " + dto.getCampusId()));

        boolean conflict = evaluationRepository
                .existsByCampusIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        campus.getId(),
                        dto.getEndDate(),
                        dto.getStartDate());

        if (conflict) {
            throw new RuntimeException(
                    "Já existe uma avaliação ativa nesse polo com datas conflitantes.");
        }

        Evaluation ev = new Evaluation();
        ev.setCode(dto.getCode());
        ev.setTitle(dto.getTitle());
        ev.setStartDate(dto.getStartDate());
        ev.setEndDate(dto.getEndDate());
        ev.setDailyStartTime(dto.getDailyStartTime());
        ev.setDailyEndTime(dto.getDailyEndTime());
        ev.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        ev.setSlotCapacity(dto.getSlotCapacity());
        ev.setCampus(campus);
        ev.setActive(true);

        return evaluationRepository.save(ev);
    }

    @Transactional(readOnly = true)
    public List<SlotResponseDto> generateSlotsForEvaluation(Long evaluationId) {
        Evaluation ev = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation não encontrada: " + evaluationId));

        List<SlotResponseDto> slots = new ArrayList<>();

        LocalDate day = ev.getStartDate();
        LocalDate end = ev.getEndDate();

        while (!day.isAfter(end)) {

            if (day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                day = day.plusDays(1);
                continue;
            }

            LocalTime t = ev.getDailyStartTime();
            LocalTime endTime = ev.getDailyEndTime();
            int duration = ev.getSlotDurationMinutes();

            while (!t.plusMinutes(duration).isAfter(endTime)) {

                LocalDateTime start = LocalDateTime.of(day, t);
                LocalDateTime endSlot = start.plusMinutes(duration);

                int used = bookingRepository.countByEvaluationIdAndSlotStartAndStatus(ev.getId(), start,
                        BookingStatus.SCHEDULED);
                int remaining = ev.getSlotCapacity() - used;
                if (remaining < 0)
                    remaining = 0;

                slots.add(SlotResponseDto.builder()
                        .start(start)
                        .end(endSlot)
                        .remainingCapacity(remaining)
                        .build());

                t = t.plusMinutes(duration);
            }

            day = day.plusDays(1);
        }

        return slots;
    }

    public List<ActiveEvaluationResponseDto> listActiveEvaluations() {

        List<Evaluation> evaluations = evaluationRepository.findByActiveTrueOrderByStartDateAsc();

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        return evaluations.stream()
                .map(ev -> new ActiveEvaluationResponseDto(
                        ev.getCode(),
                        ev.getTitle(),
                        ev.getStartDate().format(dateFmt) + " - " + ev.getEndDate().format(dateFmt),
                        ev.getDailyStartTime().format(timeFmt) + " às " + ev.getDailyEndTime().format(timeFmt),
                        ev.getCampus().getName(),
                        ev.getActive()
                )).toList();
    }

    public List<ExamPeriodResponseDto> listarPeriodosDeProva() {

        List<Evaluation> evaluations = evaluationRepository.findByActiveTrueOrderByStartDateAsc();

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        return evaluations.stream().map(ev -> {
            Map<String, Integer> bookingMap = new HashMap<>();

            evaluationRepository.countBookingsByEvaluation(ev.getId())
                    .forEach(result -> bookingMap.put(
                            (String) result[0],
                            ((Long) result[1]).intValue()
                    ));

            return new ExamPeriodResponseDto(
                    ev.getId(),
                    ev.getCode(),
                    ev.getTitle(),
                    ev.getStartDate().format(dateFmt),
                    ev.getEndDate().format(dateFmt),
                    ev.getDailyStartTime().format(timeFmt),
                    ev.getDailyEndTime().format(timeFmt),
                    ev.getSlotDurationMinutes(),
                    ev.getSlotCapacity(),
                    ev.getCampus().getId(),
                    ev.getCampus().getName(),
                    bookingMap
            );
        }).toList();
    }

}
