package co.cambridge.colegio.Mapper;

import co.cambridge.colegio.Model.DTO.EmpleadoDTO;
import co.cambridge.colegio.Model.Entity.Administrativo;
import co.cambridge.colegio.Model.Entity.Empleado;
import co.cambridge.colegio.Model.Entity.Profesor;

public class EmpleadoMapper {

    public static EmpleadoDTO toDTO(Empleado e) {
        String tipoEmpleado;
        String tipoProfesor = null;

        if (e instanceof Profesor p) {
            tipoEmpleado = "PROFESOR";
            if (p.getTipo() != null) {
                tipoProfesor = p.getTipo().name();
            }
        } else if (e instanceof Administrativo) {
            tipoEmpleado = "ADMINISTRATIVO";
        } else {
            tipoEmpleado = "DESCONOCIDO";
        }

        Long areaId = e.getArea() != null ? e.getArea().getId() : null;
        Long oficinaId = e.getOficina() != null ? e.getOficina().getId() : null;

        return new EmpleadoDTO(
                e.getId(),
                e.getNombre(),
                e.getDocumento(),
                areaId,
                oficinaId,
                tipoEmpleado,
                tipoProfesor);
        // Nota: el mapeo inverso (DTO -> entidad) lo haremos en el Service
        // porque depende de lógica (qué subclase instanciar y validaciones).
    }
}
