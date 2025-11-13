package br.com.unifaa.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unifaa.agendamento.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
