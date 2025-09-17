package co.cambridge.colegio.Mapper;

import co.cambridge.colegio.Model.Entity.Oficina;
import co.cambridge.colegio.Model.DTO.OficinaDTO;

public class OficinaMapper {
    public static OficinaDTO toDTO(Oficina o) {
        return new OficinaDTO(o.getId(), o.getCodigo(), o.getArea().getId());
    }
}
