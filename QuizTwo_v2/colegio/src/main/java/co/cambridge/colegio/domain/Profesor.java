package co.cambridge.colegio.domain;

import co.cambridge.colegio.domain.enums.TipoProfesor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("PROFESOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profesor extends Empleado {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20) // sin nullable=false
    private TipoProfesor tipo;
}
