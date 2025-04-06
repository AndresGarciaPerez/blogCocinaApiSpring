package myApp.blog.servicio;

import myApp.blog.modelo.Receta;

import java.util.List;

public interface IRecetaServicio {
    List<Receta> listarRecetas();
    Receta buscarRecetaId(Integer idReceta);
    Receta guardarReceta(Receta receta);
    void eliminarReceta(Integer idReceta);
}
