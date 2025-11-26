package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.MatriculaRequestDto;
import br.com.unifaa.agendamento.dto.MatriculaResponseDto;
import br.com.unifaa.agendamento.model.Curso;
import br.com.unifaa.agendamento.model.Matricula;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.CursoRepository;
import br.com.unifaa.agendamento.repository.MatriculaRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UserRepository userRepository;
    private final CursoRepository cursoRepository;

    public MatriculaResponseDto create(MatriculaRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        Matricula m = Matricula.builder()
                .user(user)
                .curso(curso)
                .build();

        matriculaRepository.save(m);
        return toResponse(m);
    }

    public List<MatriculaResponseDto> list() {
        return matriculaRepository.findAll().stream()
                .map(this::toResponse).toList();
    }

    public void delete(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada");
        }
        matriculaRepository.deleteById(id);
    }

    private MatriculaResponseDto toResponse(Matricula m) {
        MatriculaResponseDto r = new MatriculaResponseDto();
        r.setId(m.getId());
        r.setUserId(m.getUser().getId());
        r.setCursoId(m.getCurso().getId());
        r.setDataMatricula(m.getDataMatricula());
        return r;
    }
}

