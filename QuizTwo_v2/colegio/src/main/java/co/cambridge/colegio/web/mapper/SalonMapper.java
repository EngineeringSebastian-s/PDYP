package co.cambridge.colegio.web.mapper;

import co.cambridge.colegio.domain.Salon;
import co.cambridge.colegio.web.dto.SalonDTO;

public class SalonMapper {
    public static SalonDTO toDTO(Salon s) {
        return new SalonDTO(s.getId(), s.getCodigo());
    }

    public static Salon toEntity(SalonDTO dto) {
        Salon s = new Salon();
        s.setId(dto.id());
        s.setCodigo(dto.codigo());
        return s;
    }
}
