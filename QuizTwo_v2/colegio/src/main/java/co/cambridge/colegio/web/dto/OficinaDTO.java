package co.cambridge.colegio.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OficinaDTO(
        Long id,
        @NotBlank @Size(max = 50) String codigo,
        @NotNull Long areaId) {
}
