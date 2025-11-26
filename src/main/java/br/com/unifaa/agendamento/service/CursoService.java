package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.CursoRequestDto;
import br.com.unifaa.agendamento.dto.CursoResponseDto;
import br.com.unifaa.agendamento.model.Curso;
import br.com.unifaa.agendamento.repository.CursoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoResponseDto create(CursoRequestDto dto) {
        Curso c = Curso.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .build();
        cursoRepository.save(c);

        return toResponse(c);
    }

    public CursoResponseDto get(Long id) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));
        return toResponse(c);
    }

    public List<CursoResponseDto> list() {
        return cursoRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    public CursoResponseDto update(Long id, CursoRequestDto dto) {
        Curso c = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        c.setNome(dto.getNome());
        c.setDescricao(dto.getDescricao());
        cursoRepository.save(c);

        return toResponse(c);
    }

    public void delete(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado");
        }
        cursoRepository.deleteById(id);
    }

    private CursoResponseDto toResponse(Curso c) {
        CursoResponseDto r = new CursoResponseDto();
        r.setId(c.getId());
        r.setNome(c.getNome());
        r.setDescricao(c.getDescricao());
        r.setCriadoEm(c.getCriadoEm());
        return r;
    }
}
