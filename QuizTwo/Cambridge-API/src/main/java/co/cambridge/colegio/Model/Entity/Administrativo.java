package co.cambridge.colegio.Model.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ADMINISTRATIVO")
@Getter
@Setter
public class Administrativo extends Empleado {
    // sin campos adicionales
}
