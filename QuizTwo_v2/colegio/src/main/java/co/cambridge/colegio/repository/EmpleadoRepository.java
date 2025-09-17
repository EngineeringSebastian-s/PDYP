package co.cambridge.colegio.repository;

import co.cambridge.colegio.domain.Empleado;
import co.cambridge.colegio.domain.Area;
import co.cambridge.colegio.domain.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Empleado> findByArea(Area area);

    List<Empleado> findByOficina(Oficina oficina);

    long countByArea(co.cambridge.colegio.domain.Area area);

}
