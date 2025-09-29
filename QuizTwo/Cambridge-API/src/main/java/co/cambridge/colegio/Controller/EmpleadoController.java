package co.cambridge.colegio.Controller;

import co.cambridge.colegio.Model.DTO.EmpleadoCreateDTO;
import co.cambridge.colegio.Model.DTO.EmpleadoDTO;
import co.cambridge.colegio.Service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public List<EmpleadoDTO> listar() {
        return empleadoService.listar();
    }

    @GetMapping("/{id}")
    public EmpleadoDTO obtener(@PathVariable Long id) {
        return empleadoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoDTO crear(@Valid @RequestBody EmpleadoCreateDTO dto) {
        return empleadoService.crear(dto);
    }

    @PutMapping("/{id}")
    public EmpleadoDTO actualizar(@PathVariable Long id, @Valid @RequestBody EmpleadoCreateDTO dto) {
        return empleadoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
    }
}
