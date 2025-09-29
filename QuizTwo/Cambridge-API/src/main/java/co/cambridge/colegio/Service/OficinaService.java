package co.cambridge.colegio.Service;

import co.cambridge.colegio.Model.DTO.OficinaDTO;

import java.util.List;

public interface OficinaService {
    OficinaDTO crear(OficinaDTO dto);

    OficinaDTO actualizar(Long id, OficinaDTO dto);

    OficinaDTO obtener(Long id);

    List<OficinaDTO> listar();

    void eliminar(Long id);
}
