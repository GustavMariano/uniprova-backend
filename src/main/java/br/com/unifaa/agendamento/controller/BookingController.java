package br.com.unifaa.agendamento.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.dto.request.BookingCreateDto;
import br.com.unifaa.agendamento.dto.request.BookingUpdateDto;
import br.com.unifaa.agendamento.dto.response.AvailableEvaluationDto;
import br.com.unifaa.agendamento.dto.response.EvaluationAvailableSlotsDto;
import br.com.unifaa.agendamento.dto.response.UpcomingBookingDto;
import br.com.unifaa.agendamento.model.Booking;
import br.com.unifaa.agendamento.service.BookingService;
import br.com.unifaa.agendamento.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingCreateDto dto) {
        Booking created = bookingService.createBooking(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update")
    public ResponseEntity<Booking> update(@RequestBody BookingUpdateDto dto) {
        Booking updated = bookingService.updateBooking(
                dto.getBookingId(),
                dto.getNewSlotStart(),
                dto.getUserId());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/user/{userId}/available-slots")
    public ResponseEntity<List<EvaluationAvailableSlotsDto>> listAvailableSlotsForUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(bookingService.listAvailableSlotsForUser(userId));
    }

    @GetMapping("/upcoming/{userId}")
    public ResponseEntity<List<UpcomingBookingDto>> upcoming(@PathVariable UUID userId) {
        return ResponseEntity.ok(bookingService.listUpcomingBookings(userId));
    }

}
