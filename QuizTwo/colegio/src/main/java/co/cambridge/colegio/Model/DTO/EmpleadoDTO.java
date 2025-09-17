package co.cambridge.colegio.Model.DTO;

public record EmpleadoDTO(
        Long id,
        String nombre,
        String documento,
        Long areaId,
        Long oficinaId,
        String tipoEmpleado, // "PROFESOR" o "ADMINISTRATIVO"
        String tipoProfesor // "PLANTA"/"CONTRATISTA" o null
) {
}
