package co.cambridge.colegio.service;

import co.cambridge.colegio.web.dto.EmpleadoCreateDTO;
import co.cambridge.colegio.web.dto.EmpleadoDTO;

import java.util.List;

public interface EmpleadoService {
    EmpleadoDTO crear(EmpleadoCreateDTO dto);

    EmpleadoDTO obtener(Long id);

    List<EmpleadoDTO> listar();

    EmpleadoDTO actualizar(Long id, EmpleadoCreateDTO dto);

    void eliminar(Long id);
}
