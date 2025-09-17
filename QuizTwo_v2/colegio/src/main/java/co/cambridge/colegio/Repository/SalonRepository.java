package co.cambridge.colegio.Repository;

import co.cambridge.colegio.Model.Entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    Optional<Salon> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
