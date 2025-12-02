package co.cambridge.colegio.Controller;

import co.cambridge.colegio.Model.DTO.report.AreaEmpleadosReportDTO;
import co.cambridge.colegio.Service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Operaciones relacionadas con reportes")
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
