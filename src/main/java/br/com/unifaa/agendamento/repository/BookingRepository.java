package br.com.unifaa.agendamento.repository;

import br.com.unifaa.agendamento.enums.BookingStatus;
import br.com.unifaa.agendamento.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    int countByEvaluationIdAndSlotStartAndStatus(Long evaluationId, LocalDateTime slotStart, BookingStatus status);

    List<Booking> findByEvaluationIdOrderBySlotStartAsc(Long evaluationId);

    boolean existsByEvaluationIdAndUserId(Long evaluationId, UUID userId);

    @Query("""
                SELECT COUNT(b)
                FROM Booking b
                WHERE b.evaluation.id = :evaluationId
                AND b.slotStart = :slotStart
            """)
    long countBookingsInSlot(@Param("evaluationId") Long evaluationId,
            @Param("slotStart") LocalDateTime slotStart);

    List<Booking> findByUserIdAndStatusAndSlotStartAfterOrderBySlotStartAsc(
            UUID userId, BookingStatus status, LocalDateTime now);

}
