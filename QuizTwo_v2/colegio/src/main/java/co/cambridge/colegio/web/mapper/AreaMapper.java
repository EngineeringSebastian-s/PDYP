package co.cambridge.colegio.web.mapper;

import co.cambridge.colegio.domain.Area;
import co.cambridge.colegio.web.dto.AreaDTO;

public class AreaMapper {
    public static AreaDTO toDTO(Area a) {
        return new AreaDTO(a.getId(), a.getNombre());
    }

    public static Area toEntity(AreaDTO dto) {
        Area a = new Area();
        a.setId(dto.id());
        a.setNombre(dto.nombre());
        return a;
    }
}
