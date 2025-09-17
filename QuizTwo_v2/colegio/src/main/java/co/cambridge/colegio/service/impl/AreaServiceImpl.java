package co.cambridge.colegio.service.impl;

import co.cambridge.colegio.domain.Area;
import co.cambridge.colegio.repository.AreaRepository;
import co.cambridge.colegio.service.AreaService;
import co.cambridge.colegio.web.dto.AreaDTO;
import co.cambridge.colegio.web.error.NotFoundException;
import co.cambridge.colegio.web.mapper.AreaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public AreaDTO crear(AreaDTO dto) {
        if (areaRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new IllegalArgumentException("Ya existe un área con ese nombre");
        }
        Area a = AreaMapper.toEntity(dto);
        a.setId(null);
        return AreaMapper.toDTO(areaRepository.save(a));
    }

    @Override
    public AreaDTO actualizar(Long id, AreaDTO dto) {
        Area a = areaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + id));
        if (!a.getNombre().equalsIgnoreCase(dto.nombre())
                && areaRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new IllegalArgumentException("Ya existe un área con ese nombre");
        }
        a.setNombre(dto.nombre());
        return AreaMapper.toDTO(areaRepository.save(a));
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDTO obtener(Long id) {
        Area a = areaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + id));
        return AreaMapper.toDTO(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaDTO> listar() {
        return areaRepository.findAll().stream().map(AreaMapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!areaRepository.existsById(id)) {
            throw new NotFoundException("Área no encontrada: " + id);
        }
        areaRepository.deleteById(id);
    }
}
