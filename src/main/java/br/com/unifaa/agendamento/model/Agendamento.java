package br.com.unifaa.agendamento.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agendamentos", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "prova_id" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id", nullable = false)
    private Prova prova;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "polo_id", nullable = false)
    private Polo polo;

    @CreationTimestamp
    @Column(name = "data_agendamento", nullable = false, updatable = false)
    private Instant dataAgendamento;

    @Column(name = "tipo_status", nullable = false, length = 50)
    private String tipoStatus = "PENDENTE";
}
