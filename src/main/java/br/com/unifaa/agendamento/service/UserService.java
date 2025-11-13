package br.com.unifaa.agendamento.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.unifaa.agendamento.dto.AuthResponseDto;
import br.com.unifaa.agendamento.dto.LoginRequestDto;
import br.com.unifaa.agendamento.dto.RegisterRequestDto;
import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.model.User;
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
    @Autowired
    private PoloRepository poloRepository;

    public AuthResponseDto register(RegisterRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Polo polo = poloRepository.findById(req.getPoloId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Polo não encontrado"));

        User user = new User();
        user.setNome(req.getNome());
        user.setEmail(req.getEmail());
        user.setSenha(encoder.encode(req.getSenha()));
        user.setRole("ALUNO");
        user.setPolo(polo);

        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                Map.of("role", user.getRole(), "poloId", user.getPolo().getId()));

        return new AuthResponseDto(token, user.getRole());
    }

    public AuthResponseDto login(LoginRequestDto req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if (!encoder.matches(req.getSenha(), user.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        String token = jwtUtil.generateToken(user.getEmail(), Map.of("role", user.getRole()));
        return new AuthResponseDto(token, user.getRole());
    }
}