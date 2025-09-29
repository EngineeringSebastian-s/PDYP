package co.cambridge.colegio.Service.impl;

import co.cambridge.colegio.Exception.NotFoundException;
import co.cambridge.colegio.Mapper.OficinaMapper;
import co.cambridge.colegio.Model.DTO.OficinaDTO;
import co.cambridge.colegio.Model.Entity.Area;
import co.cambridge.colegio.Model.Entity.Oficina;
import co.cambridge.colegio.Repository.AreaRepository;
import co.cambridge.colegio.Repository.OficinaRepository;
import co.cambridge.colegio.Service.OficinaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OficinaServiceImpl implements OficinaService {

    private final OficinaRepository oficinaRepo;
    private final AreaRepository areaRepo;

    public OficinaServiceImpl(OficinaRepository oficinaRepo, AreaRepository areaRepo) {
        this.oficinaRepo = oficinaRepo;
        this.areaRepo = areaRepo;
    }

    @Override
    public OficinaDTO crear(OficinaDTO dto) {
        if (oficinaRepo.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe oficina con código " + dto.codigo());
        }
        Area area = areaRepo.findById(dto.areaId())
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + dto.areaId()));
        Oficina o = Oficina.builder()
                .codigo(dto.codigo())
                .area(area)
                .build();
        return OficinaMapper.toDTO(oficinaRepo.save(o));
    }

    @Override
    public OficinaDTO actualizar(Long id, OficinaDTO dto) {
        Oficina o = oficinaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada: " + id));
        if (!o.getCodigo().equals(dto.codigo()) && oficinaRepo.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe oficina con código " + dto.codigo());
        }
        Area area = areaRepo.findById(dto.areaId())
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + dto.areaId()));
        o.setCodigo(dto.codigo());
        o.setArea(area);
        return OficinaMapper.toDTO(oficinaRepo.save(o));
    }

    @Override
    @Transactional(readOnly = true)
    public OficinaDTO obtener(Long id) {
        return oficinaRepo.findById(id)
                .map(OficinaMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OficinaDTO> listar() {
        return oficinaRepo.findAll().stream().map(OficinaMapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!oficinaRepo.existsById(id)) {
            throw new NotFoundException("Oficina no encontrada: " + id);
        }
        oficinaRepo.deleteById(id);
    }
}
