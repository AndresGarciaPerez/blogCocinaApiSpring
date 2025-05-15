package myApp.blog.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UsuarioDTO usuario;

    public AuthResponse(String token){
        this.token = token;
    }
}
