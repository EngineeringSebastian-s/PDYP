package co.cambridge.colegio.Repository;

import co.cambridge.colegio.Model.Entity.Area;
import co.cambridge.colegio.Model.Entity.Profesor;
import co.cambridge.colegio.Model.Entity.enums.TipoProfesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    List<Profesor> findByTipo(TipoProfesor tipo);

    long countByArea(Area area);

    long countByTipoAndArea(TipoProfesor tipo,
                            Area area);

}
