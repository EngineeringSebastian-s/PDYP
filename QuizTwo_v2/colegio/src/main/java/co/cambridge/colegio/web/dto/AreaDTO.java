package co.cambridge.colegio.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AreaDTO(
        Long id,
        @NotBlank @Size(max = 120) String nombre) {
}
