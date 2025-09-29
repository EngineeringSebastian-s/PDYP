package co.cambridge.colegio.Model.Entity;

import co.cambridge.colegio.Model.Entity.enums.TipoProfesor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
