package br.com.unifaa.agendamento.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Agendamento;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByUserId(Long userId);

    List<Agendamento> findByProvaId(Long provaId);

    Optional<Agendamento> findByUserIdAndProvaId(Long userId, Long provaId);

    List<Agendamento> findByPoloId(Long poloId);

    boolean existsByUserIdAndProvaId(Long userId, Long provaId);

    List<Agendamento> findByUserIdAndProva_InicioAfter(Long userId, Instant now);

    List<Agendamento> findByUserIdAndProva_InicioBefore(Long userId, Instant now);
}
