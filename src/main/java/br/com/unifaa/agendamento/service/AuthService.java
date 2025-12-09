package br.com.unifaa.agendamento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.dto.request.AuthRequestDto;
import br.com.unifaa.agendamento.dto.request.RegisterRequestDto;
import br.com.unifaa.agendamento.dto.response.AuthResponseDto;
import br.com.unifaa.agendamento.dto.response.RegisterResponseDto;
import br.com.unifaa.agendamento.model.Campus;
import br.com.unifaa.agendamento.model.Course;
import br.com.unifaa.agendamento.model.Role;
import br.com.unifaa.agendamento.model.User;
import br.com.unifaa.agendamento.repository.CampusRepository;
import br.com.unifaa.agendamento.repository.CourseRepository;
import br.com.unifaa.agendamento.repository.RoleRepository;
import br.com.unifaa.agendamento.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CampusRepository campusRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterResponseDto register(RegisterRequestDto req) {

        Course course = courseRepository.findById(req.getCourseId()).orElse(null);
        Campus campus = campusRepository.findById(req.getCampusId()).orElse(null);
        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT não encontrada"));

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .course(course)
                .campus(campus)
                .enabled(true)
                .build();

        user.getRoles().add(studentRole);

        userRepository.save(user);

        return RegisterResponseDto.builder()
                .email(user.getEmail())
                .build();
    }

    public AuthResponseDto login(AuthRequestDto req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(Role::getName)
                .orElse("NO_ROLE");

        String course = user.getCourse().getName();

        String token = jwtService.generateToken(
                Map.of("role", role),
                user.getEmail());

        return AuthResponseDto.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .course(course)
                .campusId(user.getCampus().getId())
                .build();
    }
}
