package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.UsuarioDisciplinaRequestDto;
import br.com.unifaa.agendamento.dto.UsuarioDisciplinaResponseDto;
import br.com.unifaa.agendamento.model.Disciplina;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.model.UsuarioDisciplina;
import br.com.unifaa.agendamento.repository.DisciplinaRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import br.com.unifaa.agendamento.repository.UsuarioDisciplinaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioDisciplinaService {

    private final UsuarioDisciplinaRepository repo;
    private final DisciplinaRepository disciplinaRepository;
    private final UserRepository userRepository;

    public UsuarioDisciplinaResponseDto create(UsuarioDisciplinaRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Disciplina disc = disciplinaRepository.findById(dto.getDisciplinaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));

        UsuarioDisciplina u = UsuarioDisciplina.builder()
                .user(user)
                .disciplina(disc)
                .cursandoDisciplina(dto.isCursandoDisciplina())
                .build();

        repo.save(u);
        return toResponse(u);
    }

    public List<UsuarioDisciplinaResponseDto> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado");
        }
        repo.deleteById(id);
    }

    private UsuarioDisciplinaResponseDto toResponse(UsuarioDisciplina u) {
        UsuarioDisciplinaResponseDto r = new UsuarioDisciplinaResponseDto();
        r.setId(u.getId());
        r.setUserId(u.getUser().getId());
        r.setDisciplinaId(u.getDisciplina().getId());
        r.setCursandoDisciplina(u.isCursandoDisciplina());
        return r;
    }
}

