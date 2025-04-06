package myApp.blog.controlador;

import myApp.blog.mensajeExcepcion.RecursoNoEncontrado;
import myApp.blog.modelo.Receta;
import myApp.blog.servicio.RecetaServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("blogCocina") //Http://localhost:8080/blogCocina

//Permiso para angular
@CrossOrigin(value = "http://localhost:4200")

public class BlogControlador {

    private static final Logger logger = LoggerFactory.getLogger(BlogControlador.class);

    @Autowired
    RecetaServicio recetaServicio;

    @GetMapping("/recetas") //Http://localhost:8080/blogCocina/recetas
    public List<Receta> obtenerRecetas(){
        List<Receta> recetas = recetaServicio.listarRecetas();
        logger.info("items obtenidos: ");
        recetas.forEach(receta -> logger.info(receta.toString()));
        return recetas;
    }

    @PostMapping("/recetas")
    public Receta agregarReceta(@RequestBody Receta receta){
        logger.info("Item guardado: "+receta);
        return this.recetaServicio.guardarReceta(receta);
    }

    @GetMapping("/recetas/{id}")
    public ResponseEntity<Receta> obtenerRecetaId(@PathVariable int id){
        Receta receta = recetaServicio.buscarRecetaId(id);
        if(receta != null){
            return  ResponseEntity.ok(receta);
        }else{
            throw new RecursoNoEncontrado("No se encontro el registro: "+ receta);
        }
    }

    @PutMapping("/recetas/{id}")
    public ResponseEntity<Receta> actualizarProducto(@PathVariable int id, @RequestBody Receta recetaRecibida){
        Receta receta = this.recetaServicio.buscarRecetaId(id);
        receta.setImagen(recetaRecibida.getImagen());
        receta.setTitulo(recetaRecibida.getTitulo());
        receta.setDescripcion(recetaRecibida.getDescripcion());
        //el id ya lo tiene al ejecutar buscarRecetaPorId
        this.recetaServicio.guardarReceta(receta);
        return  ResponseEntity.ok(receta);
    }


    @DeleteMapping("/recetas/{id}")
    public ResponseEntity<Map<String,Boolean>> eliminarReceta(@PathVariable int id){
        Receta receta = recetaServicio.buscarRecetaId(id);
        if (receta == null){
            throw new RecursoNoEncontrado("No se encontro el registro: "+receta);
        }
            this.recetaServicio.eliminarReceta(receta.getIdReceta());
            Map<String,Boolean> respuesta = new HashMap<>();
            respuesta.put("Eliminado", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);
    }

}
