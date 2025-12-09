package br.com.unifaa.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unifaa.agendamento.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
