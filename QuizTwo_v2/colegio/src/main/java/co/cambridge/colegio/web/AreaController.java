package co.cambridge.colegio.web;

import co.cambridge.colegio.service.AreaService;
import co.cambridge.colegio.web.dto.AreaDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaDTO crear(@Valid @RequestBody AreaDTO dto) {
        return areaService.crear(dto);
    }

    @PutMapping("/{id}")
    public AreaDTO actualizar(@PathVariable Long id, @Valid @RequestBody AreaDTO dto) {
        return areaService.actualizar(id, dto);
    }

    @GetMapping("/{id}")
    public AreaDTO obtener(@PathVariable Long id) {
        return areaService.obtener(id);
    }

    @GetMapping
    public List<AreaDTO> listar() {
        return areaService.listar();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        areaService.eliminar(id);
    }
}
