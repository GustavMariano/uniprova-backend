package br.com.unifaa.agendamento.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unifaa.agendamento.model.Campus;
import br.com.unifaa.agendamento.service.CampusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/campuses")
@RequiredArgsConstructor
public class CampusController {

    private final CampusService campusService;

    @GetMapping
    public ResponseEntity<List<Campus>> getAllCampuses() {
        List<Campus> campuses = campusService.listAllCampuses();
        return ResponseEntity.ok(campuses);
    }
}
