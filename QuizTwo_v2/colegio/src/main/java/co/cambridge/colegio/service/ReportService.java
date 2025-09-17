package co.cambridge.colegio.service;

import co.cambridge.colegio.web.dto.report.AreaEmpleadosReportDTO;
import java.util.List;

public interface ReportService {
    List<AreaEmpleadosReportDTO> resumenAreasEmpleados();
}
