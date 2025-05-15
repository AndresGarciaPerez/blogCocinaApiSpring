package myApp.blog.dto;

import lombok.Data;
import myApp.blog.modelo.Usuario;

@Data
public class UsuarioResponse {
    private long idUsuario;
    private String username;
    private String role;

    public UsuarioResponse(Usuario usuario){
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.role = usuario.getRole();
    }

}
