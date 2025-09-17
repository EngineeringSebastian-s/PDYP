package co.cambridge.colegio.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmpleadoCreateDTO(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 40) String documento,
        @NotNull Long areaId,
        @NotNull Long oficinaId,
        @NotBlank String tipoEmpleado, // "PROFESOR" o "ADMINISTRATIVO"
        String tipoProfesor // requerido solo si tipoEmpleado = "PROFESOR"
) {
}
