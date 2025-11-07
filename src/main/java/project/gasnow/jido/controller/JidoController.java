package project.gasnow.jido.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.jido.model.dto.GasStation;
import project.gasnow.jido.model.service.jidoService;

@RestController
@RequestMapping("/api/jido")
@RequiredArgsConstructor
public class JidoController {

    private final jidoService jidoService;

    @PostMapping("/station")
    public ResponseEntity<Void> saveGasStation(@RequestBody GasStation gasStation) {
        jidoService.saveGasStation(gasStation);
        return ResponseEntity.ok().build();
    }
}
