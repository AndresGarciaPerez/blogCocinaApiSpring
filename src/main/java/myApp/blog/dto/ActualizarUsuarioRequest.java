package myApp.blog.dto;

import lombok.Data;

@Data
public class ActualizarUsuarioRequest {
    private String username;
    private String role;
}
