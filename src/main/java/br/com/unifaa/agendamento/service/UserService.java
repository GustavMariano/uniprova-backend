package br.com.unifaa.agendamento.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.AuthResponseDto;
import br.com.unifaa.agendamento.dto.LoginRequestDto;
import br.com.unifaa.agendamento.dto.LoginResponseDto;
import br.com.unifaa.agendamento.dto.RegisterRequestDto;
import br.com.unifaa.agendamento.model.Curso;
import br.com.unifaa.agendamento.model.Matricula;
import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.CursoRepository;
import br.com.unifaa.agendamento.repository.MatriculaRepository;
import br.com.unifaa.agendamento.repository.PoloRepository;
import br.com.unifaa.agendamento.repository.UserRepository;
import br.com.unifaa.agendamento.utils.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final PoloRepository poloRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;

    public AuthResponseDto register(RegisterRequestDto req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Polo polo = poloRepository.findById(req.getPoloId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polo não encontrado"));

        Curso curso = cursoRepository.findById(req.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        User user = new User();
        user.setNome(req.getNome());
        user.setEmail(req.getEmail());
        user.setSenha(encoder.encode(req.getSenha()));
        user.setRole("ALUNO");
        user.setPolo(polo);

        userRepository.save(user);

        Matricula matricula = new Matricula();
        matricula.setCurso(curso);
        matricula.setUser(user);
        matriculaRepository.save(matricula);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of(
                        "role", user.getRole(),
                        "poloId", polo.getId(),
                        "cursoId", curso.getId()));

        return new AuthResponseDto(token, user.getRole());
    }

    public LoginResponseDto login(LoginRequestDto req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if (!encoder.matches(req.getSenha(), user.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of(
                        "role", user.getRole(),
                        "poloId", user.getPolo().getId()));

        String cursoNome = matriculaRepository.findByUserId(user.getId())
                .map(m -> m.getCurso().getNome())
                .orElse(null);

        return new LoginResponseDto(
                token,
                user.getRole(),
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getPolo().getId(),
                cursoNome);
    }

}