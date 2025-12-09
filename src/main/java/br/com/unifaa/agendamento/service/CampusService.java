package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.model.Campus;
import br.com.unifaa.agendamento.repository.CampusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;

    @Transactional
    public List<Campus> listAllCampuses() {
        return campusRepository.findAll();
    }
}
