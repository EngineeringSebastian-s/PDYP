package co.cambridge.colegio.Repository;

import co.cambridge.colegio.Model.Entity.Area;
import co.cambridge.colegio.Model.Entity.Empleado;
import co.cambridge.colegio.Model.Entity.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    List<Empleado> findByArea(Area area);

    List<Empleado> findByOficina(Oficina oficina);

    long countByArea(Area area);

}
