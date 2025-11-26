package br.com.unifaa.agendamento.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Prova;

@Repository
public interface ProvaRepository extends JpaRepository<Prova, Long> {

    Optional<Prova> findByDisciplinaId(Long disciplinaId);

    List<Prova> findByProfessorId(Long professorId);

    List<Prova> findBySalaId(Long salaId);

    List<Prova> findByInicioBetween(Instant start, Instant end);

    Optional<Prova> findByDisciplinaIdAndSalaIdAndInicio(Long disciplinaId, Long salaId, Instant inicio);
}
