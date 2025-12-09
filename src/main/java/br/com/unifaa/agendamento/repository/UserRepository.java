package br.com.unifaa.agendamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unifaa.agendamento.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
