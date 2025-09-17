package co.cambridge.colegio.Model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oficinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String codigo; // identificador de oficina

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Area area;

    @OneToMany(mappedBy = "oficina", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Empleado> empleados = new ArrayList<>();
}
