package co.cambridge.colegio.Repository;

import co.cambridge.colegio.Model.Entity.Administrativo;
import co.cambridge.colegio.Model.Entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {
    long countByArea(Area area);

}
