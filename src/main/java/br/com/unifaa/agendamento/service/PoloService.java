package br.com.unifaa.agendamento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.repository.PoloRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PoloService {
    
    private final PoloRepository poloRepository;

    public List<Polo> listarTodos() {
        return poloRepository.findAll();
    }
}
