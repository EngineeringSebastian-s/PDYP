package co.cambridge.colegio.service;

import co.cambridge.colegio.web.dto.AreaDTO;

import java.util.List;

public interface AreaService {
    AreaDTO crear(AreaDTO dto);

    AreaDTO actualizar(Long id, AreaDTO dto);

    AreaDTO obtener(Long id);

    List<AreaDTO> listar();

    void eliminar(Long id);
}
