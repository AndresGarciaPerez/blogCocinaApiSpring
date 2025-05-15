package myApp.blog.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long idReceta;

    String imagen;

    String titulo;

    @Column(nullable = false, length = 255)
    String descripcion;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

}
