package myApp.blog.dto;

import lombok.Data;
import myApp.blog.modelo.Receta;

@Data
public class RecetaResponse {
    private long idReceta;
    private String imagen;
    private String titulo;
    private String descripcion;
    private UsuarioDTO usuario;

    public RecetaResponse(Receta receta){
        this.idReceta = receta.getIdReceta();
        this.imagen = receta.getImagen();
        this.titulo = receta.getTitulo();
        this.descripcion = receta.getDescripcion();

        if (receta.getUsuario() != null) {

            this.usuario = new UsuarioDTO(
                    receta.getUsuario().getIdUsuario(),
                    receta.getUsuario().getUsername(),
                    receta.getUsuario().getRole()
            );

        } else {
            this.usuario = null;
        }


    }

}
