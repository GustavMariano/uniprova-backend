package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.DisciplinaRequestDto;
import br.com.unifaa.agendamento.dto.DisciplinaResponseDto;
import br.com.unifaa.agendamento.model.Curso;
import br.com.unifaa.agendamento.model.Disciplina;
import br.com.unifaa.agendamento.repository.CursoRepository;
import br.com.unifaa.agendamento.repository.DisciplinaRepository;
import br.com.unifaa.agendamento.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public DisciplinaResponseDto create(DisciplinaRequestDto dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        Disciplina d = Disciplina.builder()
                .curso(curso)
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .build();

        disciplinaRepository.save(d);
        return toResponse(d);
    }

    public List<DisciplinaResponseDto> listByUser(Long userId) {
        var matricula = matriculaRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula do usuário não encontrada"));

        Long cursoId = matricula.getCurso().getId();

        return disciplinaRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DisciplinaResponseDto get(Long id) {
        Disciplina d = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));
        return toResponse(d);
    }

    public List<DisciplinaResponseDto> list() {
        return disciplinaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public DisciplinaResponseDto update(Long id, DisciplinaRequestDto dto) {
        Disciplina d = disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        d.setNome(dto.getNome());
        d.setDescricao(dto.getDescricao());
        d.setCurso(curso);

        disciplinaRepository.save(d);
        return toResponse(d);
    }

    public void delete(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada");
        }
        disciplinaRepository.deleteById(id);
    }

    private DisciplinaResponseDto toResponse(Disciplina d) {
        DisciplinaResponseDto r = new DisciplinaResponseDto();
        r.setId(d.getId());
        r.setNome(d.getNome());
        r.setDescricao(d.getDescricao());
        r.setCursoId(d.getCurso().getId());
        return r;
    }
}
