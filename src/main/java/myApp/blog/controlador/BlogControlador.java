package myApp.blog.controlador;

import myApp.blog.dto.RecetaResponse;
import myApp.blog.modelo.Receta;
import myApp.blog.modelo.Usuario;
import myApp.blog.repositorio.UsuarioRepositorio;
import myApp.blog.servicio.RecetaServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("blogCocina") //Http://localhost:8080/blogCocina

//Permiso para angular
//@CrossOrigin(value = "http://localhost:4200")
//@CrossOrigin(value = "https://blogcocina-spring-angular.netlify.app")

public class BlogControlador {

    private static final Logger logger = LoggerFactory.getLogger(BlogControlador.class);

    @Autowired
    RecetaServicio recetaServicio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    //LISTAR
    @GetMapping("/recetas") //Http://localhost:8080/blogCocina/recetas
    public List<RecetaResponse> obtenerRecetas(){
         return recetaServicio.listarRecetas();

    }

    //GUARDAR
    @PostMapping("/recetas")
    public RecetaResponse agregarReceta(@RequestBody Receta receta, Authentication authentication){
        return this.recetaServicio.guardarReceta(receta,authentication.getName());
    }

    //BUSCAR POR ID
    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaResponse> obtenerRecetaId(@PathVariable long id){
        RecetaResponse recetaResponse = recetaServicio.buscarRecetaId(id);
        return ResponseEntity.ok(recetaResponse);

    }

    //ACTUALIZAR
    @PutMapping("/recetas/{id}")
    public ResponseEntity<RecetaResponse> actualizarProducto(@PathVariable long id, @RequestBody Receta recetaRecibida, Authentication authentication){
        Receta receta = this.recetaServicio.obtenerRecetaEntidadPorId(id);

        Usuario usuarioAutenticado = usuarioRepositorio.findByUsername(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("Usuario no encontrado"));

        if(!receta.getUsuario().getIdUsuario().equals(usuarioAutenticado.getIdUsuario()) ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para eliminar este registro");
        }

        receta.setImagen(recetaRecibida.getImagen());
        receta.setTitulo(recetaRecibida.getTitulo());
        receta.setDescripcion(recetaRecibida.getDescripcion());

        //el id ya lo tiene al ejecutar buscarRecetaPorId
        RecetaResponse recetaActualizada = recetaServicio.guardarReceta(receta, authentication.getName());
        return  ResponseEntity.ok(recetaActualizada);
    }


    //ELIMINAR
    @DeleteMapping("/recetas/{id}")
    public ResponseEntity<Map<String,Boolean>> eliminarReceta(@PathVariable long id, Authentication authentication){
        this.recetaServicio.eliminarReceta(id, authentication.getName());

        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);

        return ResponseEntity.ok(respuesta);
    }

}
