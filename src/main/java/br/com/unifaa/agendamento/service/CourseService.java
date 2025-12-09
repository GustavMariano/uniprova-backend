package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.model.Course;
import br.com.unifaa.agendamento.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public List<Course> listAllCourses() {
        return courseRepository.findAll();
    }
}