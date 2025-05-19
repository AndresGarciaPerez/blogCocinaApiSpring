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

   // @Lob //para que este campo sea TEXT y acepte mas de mil caracteres
    @Column(nullable = false, length = 300)
    String descripcion;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

}
