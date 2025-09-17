package co.cambridge.colegio.Model.DTO.report;

public record AreaEmpleadosReportDTO(
        Long areaId,
        String areaNombre,
        long totalEmpleados,
        long totalProfesores,
        long profesoresPlanta,
        long profesoresContratistas,
        long totalAdministrativos) {
}
