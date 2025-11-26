package br.com.unifaa.agendamento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    Optional<Matricula> findByUserId(Long userId);
    List<Matricula> findByCursoId(Long cursoId);
    boolean existsByUserIdAndCursoId(Long userId, Long cursoId);
}
