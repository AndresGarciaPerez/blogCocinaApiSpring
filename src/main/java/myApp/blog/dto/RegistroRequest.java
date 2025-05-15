package myApp.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {

    @NotBlank(message = "Campo username obligatorio")
    @Size(min = 3, max = 25, message = "Debe tener un minimo de tres caracteres y un maximo de 25")
    private String username;

    @Size(min = 3, max = 25, message = "Debe tener un minimo de tres caracteres y un maximo de 25")
    private String password;

    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
