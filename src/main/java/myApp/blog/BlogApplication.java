package myApp.blog;

import myApp.blog.modelo.Receta;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);

		Receta receta = new Receta();
		receta.setImagen("qwerrt");
		receta.setTitulo("Baleadas");
		receta.setDescripcion("Este un platillo tipico Hondureño");

		//System.out.println(receta);
	}

}
