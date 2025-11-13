package br.com.unifaa.agendamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.UsuarioDisciplina;

@Repository
public interface UsuarioDisciplinaRepository extends JpaRepository<UsuarioDisciplina, Long> {
    List<UsuarioDisciplina> findByUserId(Long userId);
    List<UsuarioDisciplina> findByDisciplinaId(Long disciplinaId);
    boolean existsByDisciplinaIdAndUserId(Long disciplinaId, Long userId);
}