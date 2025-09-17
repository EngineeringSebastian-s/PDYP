package co.cambridge.colegio.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalonDTO(
        Long id,
        @NotBlank @Size(max = 50) String codigo) {
}
