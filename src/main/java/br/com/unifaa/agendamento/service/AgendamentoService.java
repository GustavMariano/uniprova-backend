package br.com.unifaa.agendamento.service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.AgendamentoResponseDto;
import br.com.unifaa.agendamento.dto.HistoricoProvaDto;
import br.com.unifaa.agendamento.dto.ProvaCalendarioDto;
import br.com.unifaa.agendamento.dto.ProvaDetalhadaDto;
import br.com.unifaa.agendamento.model.Agendamento;
import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.model.Prova;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.AgendamentoRepository;
import br.com.unifaa.agendamento.repository.PoloRepository;
import br.com.unifaa.agendamento.repository.ProvaRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

        private final AgendamentoRepository agendamentoRepository;
        private final UserRepository userRepository;
        private final ProvaRepository provaRepository;
        private final PoloRepository poloRepository;

        public AgendamentoResponseDto agendar(Long userId, Long disciplinaId, Long poloId, String dataHora) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Usuário não encontrado"));

                Polo polo = poloRepository.findById(poloId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Polo não encontrado"));

                Prova prova = provaRepository.findByDisciplinaId(disciplinaId)
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Nenhuma prova encontrada para esta disciplina"));

                if (agendamentoRepository.existsByUserIdAndProvaId(userId, prova.getId())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                        "Usuário já possui agendamento para esta prova");
                }

                Instant dataAgendada = Instant.parse(dataHora);

                Agendamento ag = new Agendamento();
                ag.setUser(user);
                ag.setProva(prova);
                ag.setPolo(polo);
                ag.setDataAgendamento(dataAgendada);
                ag.setTipoStatus("PENDENTE");

                agendamentoRepository.save(ag);

                return new AgendamentoResponseDto(
                                ag.getId(),
                                user.getId(),
                                prova.getId(),
                                polo.getId(),
                                ag.getTipoStatus(),
                                ag.getDataAgendamento());
        }

        public List<Agendamento> listAll() {
                return agendamentoRepository.findAll();
        }

        public List<ProvaCalendarioDto> listarProvasCalendario(Long userId) {

                Instant agora = Instant.now();

                return agendamentoRepository.findByUserId(userId).stream()
                                .filter(a -> a.getDataAgendamento().isAfter(agora))
                                .sorted(Comparator.comparing(Agendamento::getDataAgendamento))
                                .map(a -> ProvaCalendarioDto.builder()
                                                .title(a.getProva().getTitulo())
                                                .date(a.getDataAgendamento())
                                                .build())
                                .toList();
        }

        public List<ProvaDetalhadaDto> listarProximasProvasDetalhadas(Long userId) {

                Instant agora = Instant.now();

                return agendamentoRepository.findByUserId(userId).stream()
                                .filter(a -> a.getDataAgendamento().isAfter(agora))
                                .sorted(Comparator.comparing(Agendamento::getDataAgendamento))
                                .map(a -> {

                                        var prova = a.getProva();

                                        return ProvaDetalhadaDto.builder()
                                                        .titulo(prova.getTitulo())
                                                        .dataAgendada(a.getDataAgendamento())
                                                        .sala(prova.getSala().getNome())
                                                        .professor(prova.getProfessor().getNome())
                                                        .build();
                                })
                                .toList();
        }

        public List<HistoricoProvaDto> listarHistoricoProvas(Long userId) {

                Instant agora = Instant.now();

                return agendamentoRepository.findByUserId(userId).stream()
                                .filter(a -> a.getDataAgendamento().isBefore(agora))
                                .sorted(Comparator.comparing(Agendamento::getDataAgendamento).reversed())
                                .map(a -> {

                                        var prova = a.getProva();

                                        double nota = ThreadLocalRandom.current().nextDouble(7.0, 10.0);
                                        nota = Math.round(nota * 10.0) / 10.0;

                                        return HistoricoProvaDto.builder()
                                                        .titulo(prova.getTitulo())
                                                        .dataAgendada(a.getDataAgendamento())
                                                        .status("Aprovado")
                                                        .nota(nota)
                                                        .build();
                                })
                                .toList();
        }

        public void cancelar(Long id) {
                Agendamento agendamento = agendamentoRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Agendamento não encontrado"));

                agendamento.setTipoStatus("CANCELADO");
                agendamentoRepository.save(agendamento);
        }
}