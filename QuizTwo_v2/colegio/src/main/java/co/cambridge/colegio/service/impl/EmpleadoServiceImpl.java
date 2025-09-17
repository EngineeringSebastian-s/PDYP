package co.cambridge.colegio.service.impl;

import co.cambridge.colegio.domain.Administrativo;
import co.cambridge.colegio.domain.Area;
import co.cambridge.colegio.domain.Empleado;
import co.cambridge.colegio.domain.Oficina;
import co.cambridge.colegio.domain.Profesor;
import co.cambridge.colegio.domain.enums.TipoProfesor;
import co.cambridge.colegio.repository.AdministrativoRepository;
import co.cambridge.colegio.repository.AreaRepository;
import co.cambridge.colegio.repository.EmpleadoRepository;
import co.cambridge.colegio.repository.OficinaRepository;
import co.cambridge.colegio.repository.ProfesorRepository;
import co.cambridge.colegio.service.EmpleadoService;
import co.cambridge.colegio.web.dto.EmpleadoCreateDTO;
import co.cambridge.colegio.web.dto.EmpleadoDTO;
import co.cambridge.colegio.web.error.NotFoundException;
import co.cambridge.colegio.web.mapper.EmpleadoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepo;
    private final ProfesorRepository profesorRepo;
    private final AdministrativoRepository adminRepo;
    private final AreaRepository areaRepo;
    private final OficinaRepository oficinaRepo;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepo,
            ProfesorRepository profesorRepo,
            AdministrativoRepository adminRepo,
            AreaRepository areaRepo,
            OficinaRepository oficinaRepo) {
        this.empleadoRepo = empleadoRepo;
        this.profesorRepo = profesorRepo;
        this.adminRepo = adminRepo;
        this.areaRepo = areaRepo;
        this.oficinaRepo = oficinaRepo;
    }

    @Override
    public EmpleadoDTO crear(EmpleadoCreateDTO dto) {
        if (empleadoRepo.existsByDocumento(dto.documento())) {
            throw new IllegalArgumentException("Ya existe un empleado con documento " + dto.documento());
        }

        Area area = areaRepo.findById(dto.areaId())
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + dto.areaId()));
        Oficina oficina = oficinaRepo.findById(dto.oficinaId())
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada: " + dto.oficinaId()));

        String tipo = dto.tipoEmpleado().trim().toUpperCase();

        if ("PROFESOR".equals(tipo)) {
            if (dto.tipoProfesor() == null || dto.tipoProfesor().isBlank()) {
                throw new IllegalArgumentException(
                        "tipoProfesor es requerido para tipoEmpleado=PROFESOR (PLANTA o CONTRATISTA)");
            }
            TipoProfesor tp;
            try {
                tp = TipoProfesor.valueOf(dto.tipoProfesor().trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("tipoProfesor debe ser PLANTA o CONTRATISTA");
            }

            Profesor p = new Profesor();
            p.setTipo(tp);
            p.setNombre(dto.nombre());
            p.setDocumento(dto.documento());
            p.setArea(area);
            p.setOficina(oficina);

            return EmpleadoMapper.toDTO(profesorRepo.save(p));

        } else if ("ADMINISTRATIVO".equals(tipo)) {

            Administrativo a = new Administrativo();
            a.setNombre(dto.nombre());
            a.setDocumento(dto.documento());
            a.setArea(area);
            a.setOficina(oficina);

            return EmpleadoMapper.toDTO(adminRepo.save(a));

        } else {
            throw new IllegalArgumentException("tipoEmpleado debe ser PROFESOR o ADMINISTRATIVO");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoDTO obtener(Long id) {
        Empleado e = empleadoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado: " + id));
        return EmpleadoMapper.toDTO(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDTO> listar() {
        return empleadoRepo.findAll().stream().map(EmpleadoMapper::toDTO).toList();
    }

    @Override
    public EmpleadoDTO actualizar(Long id, EmpleadoCreateDTO dto) {
        Empleado actual = empleadoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado: " + id));

        if (!actual.getDocumento().equals(dto.documento()) && empleadoRepo.existsByDocumento(dto.documento())) {
            throw new IllegalArgumentException("Ya existe un empleado con documento " + dto.documento());
        }

        Area area = areaRepo.findById(dto.areaId())
                .orElseThrow(() -> new NotFoundException("Área no encontrada: " + dto.areaId()));
        Oficina oficina = oficinaRepo.findById(dto.oficinaId())
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada: " + dto.oficinaId()));

        String tipo = dto.tipoEmpleado().trim().toUpperCase();

        if ("PROFESOR".equals(tipo)) {
            if (dto.tipoProfesor() == null || dto.tipoProfesor().isBlank()) {
                throw new IllegalArgumentException(
                        "tipoProfesor es requerido para tipoEmpleado=PROFESOR (PLANTA o CONTRATISTA)");
            }
            TipoProfesor tp;
            try {
                tp = TipoProfesor.valueOf(dto.tipoProfesor().trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("tipoProfesor debe ser PLANTA o CONTRATISTA");
            }

            Profesor p = (actual instanceof Profesor) ? (Profesor) actual : new Profesor();
            p.setTipo(tp);
            p.setNombre(dto.nombre());
            p.setDocumento(dto.documento());
            p.setArea(area);
            p.setOficina(oficina);

            if (!(actual instanceof Profesor)) {
                // Si era Administrativo y ahora es Profesor, eliminamos el registro anterior
                empleadoRepo.deleteById(actual.getId());
            }
            return EmpleadoMapper.toDTO(profesorRepo.save(p));

        } else if ("ADMINISTRATIVO".equals(tipo)) {

            Administrativo a = (actual instanceof Administrativo) ? (Administrativo) actual : new Administrativo();
            a.setNombre(dto.nombre());
            a.setDocumento(dto.documento());
            a.setArea(area);
            a.setOficina(oficina);

            if (!(actual instanceof Administrativo)) {
                // Si era Profesor y ahora es Administrativo, eliminamos el registro anterior
                empleadoRepo.deleteById(actual.getId());
            }
            return EmpleadoMapper.toDTO(adminRepo.save(a));

        } else {
            throw new IllegalArgumentException("tipoEmpleado debe ser PROFESOR o ADMINISTRATIVO");
        }
    }

    @Override
    public void eliminar(Long id) {
        if (!empleadoRepo.existsById(id)) {
            throw new NotFoundException("Empleado no encontrado: " + id);
        }
        empleadoRepo.deleteById(id);
    }
}
