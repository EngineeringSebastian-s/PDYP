package co.cambridge.colegio.Service;

import co.cambridge.colegio.Model.DTO.AreaDTO;

import java.util.List;

public interface AreaService {
    AreaDTO crear(AreaDTO dto);

    AreaDTO actualizar(Long id, AreaDTO dto);

    AreaDTO obtener(Long id);

    List<AreaDTO> listar();

    void eliminar(Long id);
}
