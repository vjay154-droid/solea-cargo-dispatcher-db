package no.solea.cargodispatcher.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "planet")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double distance;
}
