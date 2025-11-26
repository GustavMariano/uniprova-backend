package br.com.unifaa.agendamento.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.SalaRequestDto;
import br.com.unifaa.agendamento.dto.SalaResponseDto;
import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.model.Sala;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.PoloRepository;
import br.com.unifaa.agendamento.repository.SalaRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final PoloRepository poloRepository;
    private final UserRepository userRepository;

    public SalaResponseDto create(SalaRequestDto dto) {

        if (salaRepository.existsByPoloIdAndNome(dto.getPoloId(), dto.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma sala com esse nome neste polo");
        }

        Polo polo = poloRepository.findById(dto.getPoloId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polo não encontrado"));

        Sala sala = Sala.builder()
                .polo(polo)
                .nome(dto.getNome())
                .capacidade(dto.getCapacidade())
                .recursos(dto.getRecursos())
                .status(dto.getStatus())
                .build();

        salaRepository.save(sala);

        return toDto(sala);
    }

    public List<SalaResponseDto> findAll() {
        return salaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public SalaResponseDto findById(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

        return toDto(sala);
    }

    public List<SalaResponseDto> listByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Long poloId = user.getPolo().getId();

        return salaRepository.findByPoloId(poloId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SalaResponseDto toResponse(Sala sala) {
        return SalaResponseDto.builder()
                .id(sala.getId())
                .poloId(sala.getPolo().getId())
                .nome(sala.getNome())
                .capacidade(sala.getCapacidade())
                .recursos(sala.getRecursos())
                .build();
    }

    public SalaResponseDto update(Long id, SalaRequestDto dto) {

        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

        if (!sala.getNome().equals(dto.getNome()) ||
                !sala.getPolo().getId().equals(dto.getPoloId())) {

            if (salaRepository.existsByPoloIdAndNome(dto.getPoloId(), dto.getNome())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existe uma sala com esse nome neste polo");
            }
        }

        Polo polo = poloRepository.findById(dto.getPoloId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polo não encontrado"));

        sala.setPolo(polo);
        sala.setNome(dto.getNome());
        sala.setCapacidade(dto.getCapacidade());
        sala.setRecursos(dto.getRecursos());

        salaRepository.save(sala);

        return toDto(sala);
    }

    public void delete(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada");
        }
        salaRepository.deleteById(id);
    }

    private SalaResponseDto toDto(Sala sala) {
        return SalaResponseDto.builder()
                .id(sala.getId())
                .poloId(sala.getPolo().getId())
                .nome(sala.getNome())
                .capacidade(sala.getCapacidade())
                .recursos(sala.getRecursos())
                .status(sala.getStatus())
                .build();
    }
}
