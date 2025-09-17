package co.cambridge.colegio.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "empleados", indexes = {
        @Index(name = "idx_empleado_documento", columnList = "documento", unique = true)
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_empleado", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, length = 40, unique = true)
    private String documento; // cédula u otro

    @ManyToOne(fetch = FetchType.LAZY)
    private Area area; // empleado pertenece a un área

    @ManyToOne(fetch = FetchType.LAZY)
    private Oficina oficina; // oficina asignada (puede tener varios empleados)
}
