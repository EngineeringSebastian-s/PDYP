package co.cambridge.colegio.Service.impl;

import co.cambridge.colegio.Model.Entity.Salon;
import co.cambridge.colegio.Repository.SalonRepository;
import co.cambridge.colegio.Service.SalonService;
import co.cambridge.colegio.Model.DTO.SalonDTO;
import co.cambridge.colegio.Exception.NotFoundException;
import co.cambridge.colegio.Mapper.SalonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepo;

    public SalonServiceImpl(SalonRepository salonRepo) {
        this.salonRepo = salonRepo;
    }

    @Override
    public SalonDTO crear(SalonDTO dto) {
        if (salonRepo.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe un salón con código " + dto.codigo());
        }
        Salon s = SalonMapper.toEntity(dto);
        s.setId(null);
        return SalonMapper.toDTO(salonRepo.save(s));
    }

    @Override
    public SalonDTO actualizar(Long id, SalonDTO dto) {
        Salon s = salonRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Salón no encontrado: " + id));
        if (!s.getCodigo().equals(dto.codigo()) && salonRepo.existsByCodigo(dto.codigo())) {
            throw new IllegalArgumentException("Ya existe un salón con código " + dto.codigo());
        }
        s.setCodigo(dto.codigo());
        return SalonMapper.toDTO(salonRepo.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public SalonDTO obtener(Long id) {
        return salonRepo.findById(id)
                .map(SalonMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Salón no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalonDTO> listar() {
        return salonRepo.findAll().stream().map(SalonMapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!salonRepo.existsById(id)) {
            throw new NotFoundException("Salón no encontrado: " + id);
        }
        salonRepo.deleteById(id);
    }
}
