package br.com.unifaa.agendamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByPoloId(Long poloId);
}
