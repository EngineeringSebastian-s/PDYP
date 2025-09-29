package co.cambridge.colegio.Mapper;

import co.cambridge.colegio.Model.DTO.AreaDTO;
import co.cambridge.colegio.Model.Entity.Area;

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
