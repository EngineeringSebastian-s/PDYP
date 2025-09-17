package co.cambridge.colegio.repository;

import co.cambridge.colegio.domain.Administrativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {
    long countByArea(co.cambridge.colegio.domain.Area area);

}
