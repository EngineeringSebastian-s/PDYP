package co.cambridge.colegio.web;

import co.cambridge.colegio.service.ReportService;
import co.cambridge.colegio.web.dto.report.AreaEmpleadosReportDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/areas-empleados")
    public List<AreaEmpleadosReportDTO> areasEmpleados() {
        return reportService.resumenAreasEmpleados();
    }
}
