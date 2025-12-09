package br.com.unifaa.agendamento.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.dto.request.BookingCreateDto;
import br.com.unifaa.agendamento.dto.response.EvaluationAvailableSlotsDto;
import br.com.unifaa.agendamento.dto.response.SlotResponseDto;
import br.com.unifaa.agendamento.dto.response.UpcomingBookingDto;
import br.com.unifaa.agendamento.enums.BookingStatus;
import br.com.unifaa.agendamento.model.Booking;
import br.com.unifaa.agendamento.model.Evaluation;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.BookingRepository;
import br.com.unifaa.agendamento.repository.EvaluationRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final EvaluationService evaluationService;

    @Transactional
    public Booking createBooking(BookingCreateDto dto) {

        Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluation não encontrada"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getCampus().getId().equals(evaluation.getCampus().getId())) {
            throw new RuntimeException(
                    "O campus do usuário (" + user.getCampus().getName() +
                            ") é diferente do campus da avaliação (" + evaluation.getCampus().getName() + ")");
        }

        LocalDateTime requestedStart = dto.getSlotStart();

        if (requestedStart.toLocalDate().isBefore(evaluation.getStartDate())
                || requestedStart.toLocalDate().isAfter(evaluation.getEndDate())) {
            throw new RuntimeException("Data fora do período da avaliação");
        }

        if (requestedStart.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Não é permitido agendar aos domingos");
        }

        if (requestedStart.toLocalTime().isBefore(evaluation.getDailyStartTime())
                || requestedStart.toLocalTime().plusMinutes(evaluation.getSlotDurationMinutes())
                        .isAfter(evaluation.getDailyEndTime())) {
            throw new RuntimeException("Horário fora do horário diário permitido");
        }

        int duration = evaluation.getSlotDurationMinutes();
        LocalTime dailyStart = evaluation.getDailyStartTime();
        long minutesFromStart = Duration.between(dailyStart, requestedStart.toLocalTime()).toMinutes();

        if (minutesFromStart % duration != 0) {
            throw new RuntimeException("Horário inválido. Deve seguir intervalos de "
                    + duration + " minutos. Exemplo: 08:00, 09:10, 10:20...");
        }

        LocalDateTime calculatedEnd = requestedStart.plusMinutes(duration);

        int used = bookingRepository.countByEvaluationIdAndSlotStartAndStatus(
                evaluation.getId(), requestedStart, BookingStatus.SCHEDULED);

        if (used >= evaluation.getSlotCapacity()) {
            throw new RuntimeException("Esse horário já está cheio");
        }

        boolean alreadyBooked = bookingRepository.existsByEvaluationIdAndUserId(
                evaluation.getId(), user.getId());

        if (alreadyBooked) {
            throw new RuntimeException("Usuário já possui um agendamento para essa avaliação");
        }

        Booking booking = Booking.builder()
                .evaluation(evaluation)
                .user(user)
                .slotStart(requestedStart)
                .slotEnd(calculatedEnd)
                .status(BookingStatus.SCHEDULED)
                .build();

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(UUID bookingId, LocalDateTime newSlotStart, UUID userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para alterar este agendamento");
        }

        Evaluation evaluation = booking.getEvaluation();
        int duration = evaluation.getSlotDurationMinutes();

        if (newSlotStart.toLocalDate().isBefore(evaluation.getStartDate())
                || newSlotStart.toLocalDate().isAfter(evaluation.getEndDate())) {
            throw new RuntimeException("Nova data fora do período da avaliação");
        }

        if (newSlotStart.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Não é permitido agendar aos domingos");
        }

        if (newSlotStart.toLocalTime().isBefore(evaluation.getDailyStartTime())
                || newSlotStart.toLocalTime().plusMinutes(duration)
                        .isAfter(evaluation.getDailyEndTime())) {
            throw new RuntimeException("Horário fora do horário diário permitido");
        }

        LocalTime dayStart = evaluation.getDailyStartTime();
        long diffMinutes = Duration.between(dayStart, newSlotStart.toLocalTime()).toMinutes();

        if (diffMinutes % duration != 0) {
            throw new RuntimeException(
                    "Horário inválido. Deve seguir intervalos de " + duration +
                            " minutos. Exemplo: 08:00, 09:10, 10:20...");
        }

        LocalDateTime newSlotEnd = newSlotStart.plusMinutes(duration);

        int used = bookingRepository.countByEvaluationIdAndSlotStartAndStatus(
                evaluation.getId(), newSlotStart, BookingStatus.SCHEDULED);

        if (booking.getSlotStart().equals(newSlotStart)) {
            used -= 1;
        }

        if (used >= evaluation.getSlotCapacity()) {
            throw new RuntimeException("Esse horário já está cheio");
        }

        booking.setSlotStart(newSlotStart);
        booking.setSlotEnd(newSlotEnd);

        return bookingRepository.save(booking);
    }

    @Transactional
    public List<EvaluationAvailableSlotsDto> listAvailableSlotsForUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Long campusId = user.getCampus().getId();

        List<Evaluation> evaluations = evaluationRepository.findByCampusIdAndActiveTrue(campusId);

        List<EvaluationAvailableSlotsDto> response = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Evaluation ev : evaluations) {

            List<SlotResponseDto> slots = evaluationService.generateSlotsForEvaluation(ev.getId());

            List<SlotResponseDto> availableSlots = slots.stream()
                    .filter(slot -> slot.getRemainingCapacity() > 0)
                    .filter(slot -> slot.getStart().isAfter(now))
                    .toList();

            if (!availableSlots.isEmpty()) {
                response.add(
                        new EvaluationAvailableSlotsDto(
                                ev.getId(),
                                ev.getCode() + " - " + user.getCourse().getName(),
                                availableSlots));
            }
        }

        return response;
    }

    @Transactional
    public List<UpcomingBookingDto> listUpcomingBookings(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = bookingRepository
                .findByUserIdAndStatusAndSlotStartAfterOrderBySlotStartAsc(
                        user.getId(),
                        BookingStatus.SCHEDULED,
                        now);

        return bookings.stream()
                .map(b -> new UpcomingBookingDto(
                        b.getId(),
                        b.getEvaluation().getCode() + " - " + b.getUser().getCourse().getName(),
                        b.getSlotStart(),
                        b.getSlotEnd(),
                        b.getEvaluation().getCampus().getName()))
                .toList();

    }

}
