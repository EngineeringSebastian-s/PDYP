package co.cambridge.colegio.Service;

import co.cambridge.colegio.Model.DTO.report.AreaEmpleadosReportDTO;

import java.util.List;

public interface ReportService {
    List<AreaEmpleadosReportDTO> resumenAreasEmpleados();
}
