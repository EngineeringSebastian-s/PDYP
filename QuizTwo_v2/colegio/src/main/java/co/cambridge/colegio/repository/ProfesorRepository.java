package co.cambridge.colegio.repository;

import co.cambridge.colegio.domain.Profesor;
import co.cambridge.colegio.domain.enums.TipoProfesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    List<Profesor> findByTipo(TipoProfesor tipo);

    long countByArea(co.cambridge.colegio.domain.Area area);

    long countByTipoAndArea(co.cambridge.colegio.domain.enums.TipoProfesor tipo,
            co.cambridge.colegio.domain.Area area);

}
