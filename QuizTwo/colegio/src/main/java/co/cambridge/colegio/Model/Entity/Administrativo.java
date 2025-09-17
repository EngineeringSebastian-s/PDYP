package co.cambridge.colegio.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("ADMINISTRATIVO")
@Getter
@Setter
public class Administrativo extends Empleado {
    // sin campos adicionales
}
