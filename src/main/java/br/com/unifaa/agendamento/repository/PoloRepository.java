package br.com.unifaa.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Polo;

@Repository
public interface PoloRepository extends JpaRepository<Polo, Long> {
    
}
