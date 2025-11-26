package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.model.Disciplina;
import br.com.unifaa.agendamento.model.Professor;
import br.com.unifaa.agendamento.model.Prova;
import br.com.unifaa.agendamento.model.Sala;
import br.com.unifaa.agendamento.repository.DisciplinaRepository;
import br.com.unifaa.agendamento.repository.ProfessorRepository;
import br.com.unifaa.agendamento.repository.ProvaRepository;
import br.com.unifaa.agendamento.repository.SalaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvaService {

    private final ProvaRepository provaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final SalaRepository salaRepository;

    public Prova create(Prova prova) {

        Disciplina disciplina = disciplinaRepository.findById(prova.getDisciplina().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));

        Professor professor = professorRepository.findById(prova.getProfessor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));

        Sala sala = salaRepository.findById(prova.getSala().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

        prova.setDisciplina(disciplina);
        prova.setProfessor(professor);
        prova.setSala(sala);

        return provaRepository.save(prova);
    }

    public List<Prova> listAll() {
        return provaRepository.findAll();
    }

    public Prova getById(Long id) {
        return provaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prova não encontrada"));
    }

    public Prova update(Long id, Prova dto) {

        Prova prova = getById(id);

        prova.setTitulo(dto.getTitulo());
        prova.setDescricao(dto.getDescricao());
        prova.setInicio(dto.getInicio());
        prova.setFim(dto.getFim());

        if (dto.getDisciplina() != null) {
            Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplina().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada"));
            prova.setDisciplina(disciplina);
        }

        if (dto.getProfessor() != null) {
            Professor professor = professorRepository.findById(dto.getProfessor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
            prova.setProfessor(professor);
        }

        if (dto.getSala() != null) {
            Sala sala = salaRepository.findById(dto.getSala().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
            prova.setSala(sala);
        }

        return provaRepository.save(prova);
    }

    public void delete(Long id) {
        Prova prova = getById(id);
        provaRepository.delete(prova);
    }
}
