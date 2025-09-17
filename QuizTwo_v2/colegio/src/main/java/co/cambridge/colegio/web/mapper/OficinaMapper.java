package co.cambridge.colegio.web.mapper;

import co.cambridge.colegio.domain.Oficina;
import co.cambridge.colegio.web.dto.OficinaDTO;

public class OficinaMapper {
    public static OficinaDTO toDTO(Oficina o) {
        return new OficinaDTO(o.getId(), o.getCodigo(), o.getArea().getId());
    }
}
