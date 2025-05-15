package myApp.blog.servicio;

import myApp.blog.dto.RecetaDto;
import myApp.blog.dto.RecetaResponse;
import myApp.blog.dto.UsuarioResponse;
import myApp.blog.modelo.Receta;

import java.util.List;

public interface IRecetaServicio {
    List<RecetaResponse> listarRecetas();
    Receta obtenerRecetaEntidadPorId(Long idReceta);
    RecetaResponse buscarRecetaId(Long idReceta);
    RecetaResponse guardarReceta(Receta receta, String username);
    void eliminarReceta(Long idReceta, String username);
}
