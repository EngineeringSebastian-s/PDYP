package co.cambridge.colegio.web;

import co.cambridge.colegio.service.SalonService;
import co.cambridge.colegio.web.dto.SalonDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salones")
public class SalonController {

    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping
    public List<SalonDTO> listar() {
        return salonService.listar();
    }

    @GetMapping("/{id}")
    public SalonDTO obtener(@PathVariable Long id) {
        return salonService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalonDTO crear(@Valid @RequestBody SalonDTO dto) {
        return salonService.crear(dto);
    }

    @PutMapping("/{id}")
    public SalonDTO actualizar(@PathVariable Long id, @Valid @RequestBody SalonDTO dto) {
        return salonService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        salonService.eliminar(id);
    }
}
