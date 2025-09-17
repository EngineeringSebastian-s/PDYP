package co.cambridge.colegio.web.dto.report;

public record AreaEmpleadosReportDTO(
        Long areaId,
        String areaNombre,
        long totalEmpleados,
        long totalProfesores,
        long profesoresPlanta,
        long profesoresContratistas,
        long totalAdministrativos) {
}
