package br.com.unifaa.agendamento.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.ProfessorRequestDto;
import br.com.unifaa.agendamento.dto.ProfessorResponseDto;
import br.com.unifaa.agendamento.model.Professor;
import br.com.unifaa.agendamento.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorResponseDto create(ProfessorRequestDto dto) {

        if (professorRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Professor professor = Professor.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .status(dto.getStatus())
                .departamento(dto.getDepartamento())
                .build();

        professorRepository.save(professor);

        return toResponse(professor);
    }

    public List<ProfessorResponseDto> findAll() {
        return professorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProfessorResponseDto findById(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));

        return toResponse(professor);
    }

    public ProfessorResponseDto update(Long id, ProfessorRequestDto dto) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));

        if (!professor.getEmail().equals(dto.getEmail()) &&
            professorRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        professor.setTelefone(dto.getTelefone());
        professor.setStatus(dto.getStatus());
        professor.setDepartamento(dto.getDepartamento());

        professorRepository.save(professor);

        return toResponse(professor);
    }

    public void delete(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado");
        }

        professorRepository.deleteById(id);
    }

    private ProfessorResponseDto toResponse(Professor professor) {
        return ProfessorResponseDto.builder()
                .id(professor.getId())
                .nome(professor.getNome())
                .email(professor.getEmail())
                .telefone(professor.getTelefone())
                .status(professor.getStatus())
                .departamento(professor.getDepartamento())
                .criadoEm(professor.getCriadoEm())
                .build();
    }
}
