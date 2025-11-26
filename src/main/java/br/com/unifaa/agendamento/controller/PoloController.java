package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.model.Polo;
import br.com.unifaa.agendamento.service.PoloService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/polos")
@RequiredArgsConstructor
public class PoloController {
    
    private final PoloService poloService;

    @GetMapping
    public List<Polo> listar() {
        return poloService.listarTodos();
    }
}
