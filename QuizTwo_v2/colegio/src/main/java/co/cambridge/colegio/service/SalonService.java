package co.cambridge.colegio.service;

import co.cambridge.colegio.web.dto.SalonDTO;
import java.util.List;

public interface SalonService {
    SalonDTO crear(SalonDTO dto);

    SalonDTO actualizar(Long id, SalonDTO dto);

    SalonDTO obtener(Long id);

    List<SalonDTO> listar();

    void eliminar(Long id);
}
