package myApp.blog.repositorio;

import myApp.blog.modelo.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepositorio extends JpaRepository<Receta, Long> {
}
