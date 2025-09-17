package co.cambridge.colegio.repository;

import co.cambridge.colegio.domain.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    Optional<Salon> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
