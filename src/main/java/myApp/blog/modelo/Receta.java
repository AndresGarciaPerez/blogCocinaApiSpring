package myApp.blog.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idReceta;
    String imagen;
    String titulo;
    String descripcion;

}
