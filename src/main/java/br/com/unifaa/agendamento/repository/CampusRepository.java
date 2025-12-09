package br.com.unifaa.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unifaa.agendamento.model.Campus;

public interface CampusRepository extends JpaRepository<Campus, Long> {
}
