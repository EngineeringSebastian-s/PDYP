package co.cambridge.colegio.Service;

import co.cambridge.colegio.Model.DTO.EmpleadoCreateDTO;
import co.cambridge.colegio.Model.DTO.EmpleadoDTO;

import java.util.List;

public interface EmpleadoService {
    EmpleadoDTO crear(EmpleadoCreateDTO dto);

    EmpleadoDTO obtener(Long id);

    List<EmpleadoDTO> listar();

    EmpleadoDTO actualizar(Long id, EmpleadoCreateDTO dto);

    void eliminar(Long id);
}
