package co.cambridge.colegio.Repository;

import co.cambridge.colegio.Model.Entity.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OficinaRepository extends JpaRepository<Oficina, Long> {
    Optional<Oficina> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
