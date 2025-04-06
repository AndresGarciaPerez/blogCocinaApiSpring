package myApp.blog.servicio;


import myApp.blog.modelo.Receta;
import myApp.blog.repositorio.RecetaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecetaServicio implements IRecetaServicio{

    @Autowired
    private RecetaRepositorio recetaRepositorio;

    @Override
    public List<Receta> listarRecetas() {
        return this.recetaRepositorio.findAll();
    }

    @Override
    public Receta buscarRecetaId(Integer idReceta) {
        return this.recetaRepositorio.findById(idReceta).orElse(null);
    }

    @Override
    public Receta guardarReceta(Receta receta) {
        return this.recetaRepositorio.save(receta);
    }

    @Override
    public void eliminarReceta(Integer idReceta) {
        recetaRepositorio.deleteById(idReceta);
    }
}
