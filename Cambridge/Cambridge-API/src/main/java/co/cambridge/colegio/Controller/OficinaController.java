package co.cambridge.colegio.Controller;

import co.cambridge.colegio.Model.DTO.OficinaDTO;
import co.cambridge.colegio.Service.OficinaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oficinas")
@Tag(name = "Oficinas", description = "Operaciones relacionadas con oficinas")
public class OficinaController {

    private final OficinaService oficinaService;

    public OficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public List<OficinaDTO> listar() {
        return oficinaService.listar();
    }

    @GetMapping("/{id}")
    public OficinaDTO obtener(@PathVariable Long id) {
        return oficinaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OficinaDTO crear(@Valid @RequestBody OficinaDTO dto) {
        return oficinaService.crear(dto);
    }

    @PutMapping("/{id}")
    public OficinaDTO actualizar(@PathVariable Long id, @Valid @RequestBody OficinaDTO dto) {
        return oficinaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        oficinaService.eliminar(id);
    }
}
