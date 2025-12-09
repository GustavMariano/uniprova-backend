package br.com.unifaa.agendamento.repository;

import br.com.unifaa.agendamento.model.Evaluation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByCampusIdAndActiveTrue(Long campusId);

    boolean existsByCampusIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long campusId, LocalDate endDate, LocalDate startDate);

    Optional<Evaluation> findByIdAndCampusIdAndActiveTrue(Long evaluationId, Long campusId);

    List<Evaluation> findByActiveTrueOrderByStartDateAsc();

    @Query("""
                SELECT
                    CONCAT(
                        b.evaluation.id, '-',
                        TO_CHAR(b.slotStart, 'YYYY-MM-DD'), '-',
                        TO_CHAR(b.slotStart, 'HH24:MI')
                    ) AS slotKey,
                    COUNT(b.id)
                FROM Booking b
                WHERE b.evaluation.id = :evaluationId
                GROUP BY slotKey
            """)
    List<Object[]> countBookingsByEvaluation(@Param("evaluationId") Long evaluationId);

}
