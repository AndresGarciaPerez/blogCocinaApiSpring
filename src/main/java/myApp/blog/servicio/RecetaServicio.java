package myApp.blog.servicio;

import myApp.blog.dto.RecetaResponse;
import myApp.blog.mensajeExcepcion.RecursoNoEncontrado;
import myApp.blog.modelo.Receta;
import myApp.blog.modelo.Usuario;
import myApp.blog.repositorio.RecetaRepositorio;
import myApp.blog.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecetaServicio implements IRecetaServicio{

    @Autowired
    private RecetaRepositorio recetaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<RecetaResponse> listarRecetas() {
        return recetaRepositorio.findAll().stream().map(RecetaResponse::new).toList();
    }

    @Override
    public Receta obtenerRecetaEntidadPorId(Long idReceta) {
        return recetaRepositorio.findById(idReceta)
                .orElseThrow(() -> new RecursoNoEncontrado("Receta no encontrada con ID: " + idReceta));
    }

    @Override
    public RecetaResponse buscarRecetaId(Long idReceta) {
        Receta receta = recetaRepositorio.findById(idReceta)
                .orElseThrow( () -> new RecursoNoEncontrado("No se encontro el recurso"));

        return new RecetaResponse(receta);
    }

   @Override
   public RecetaResponse guardarReceta(Receta receta, String username) {
       Usuario usuario = usuarioRepositorio.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
       receta.setUsuario(usuario);
       Receta recetaGuardada = recetaRepositorio.save(receta);
       return new RecetaResponse(recetaGuardada);
   }

    @Override
    public void eliminarReceta(Long idReceta, String username) {
        Receta receta = recetaRepositorio.findById(idReceta)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));

        if(!receta.getUsuario().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para eliminar este registro");
        }
        recetaRepositorio.deleteById(receta.getIdReceta());
    }
}
