package co.cambridge.colegio.Mapper;

import co.cambridge.colegio.Model.DTO.OficinaDTO;
import co.cambridge.colegio.Model.Entity.Oficina;

public class OficinaMapper {
    public static OficinaDTO toDTO(Oficina o) {
        return new OficinaDTO(o.getId(), o.getCodigo(), o.getArea().getId());
    }
}
