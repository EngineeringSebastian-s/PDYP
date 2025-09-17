package co.cambridge.colegio.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AreaDTO(
        Long id,
        @NotBlank @Size(max = 120) String nombre) {
}
