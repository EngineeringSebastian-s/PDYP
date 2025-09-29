package co.cambridge.colegio.Service.impl;

import co.cambridge.colegio.Model.DTO.report.AreaEmpleadosReportDTO;
import co.cambridge.colegio.Model.Entity.Area;
import co.cambridge.colegio.Model.Entity.enums.TipoProfesor;
import co.cambridge.colegio.Repository.AdministrativoRepository;
import co.cambridge.colegio.Repository.AreaRepository;
import co.cambridge.colegio.Repository.EmpleadoRepository;
import co.cambridge.colegio.Repository.ProfesorRepository;
import co.cambridge.colegio.Service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final AreaRepository areaRepo;
    private final EmpleadoRepository empleadoRepo;
    private final ProfesorRepository profesorRepo;
    private final AdministrativoRepository adminRepo;

    public ReportServiceImpl(AreaRepository areaRepo,
                             EmpleadoRepository empleadoRepo,
                             ProfesorRepository profesorRepo,
                             AdministrativoRepository adminRepo) {
        this.areaRepo = areaRepo;
        this.empleadoRepo = empleadoRepo;
        this.profesorRepo = profesorRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public List<AreaEmpleadosReportDTO> resumenAreasEmpleados() {
        List<Area> areas = areaRepo.findAll();

        return areas.stream().map(area -> {
            long totalEmpleados = empleadoRepo.countByArea(area);
            long totalProfesores = profesorRepo.countByArea(area);
            long profesoresPlanta = profesorRepo.countByTipoAndArea(TipoProfesor.PLANTA, area);
            long profesoresContratistas = profesorRepo.countByTipoAndArea(TipoProfesor.CONTRATISTA, area);
            long totalAdministrativos = adminRepo.countByArea(area);

            return new AreaEmpleadosReportDTO(
                    area.getId(),
                    area.getNombre(),
                    totalEmpleados,
                    totalProfesores,
                    profesoresPlanta,
                    profesoresContratistas,
                    totalAdministrativos);
        }).toList();
    }
}
