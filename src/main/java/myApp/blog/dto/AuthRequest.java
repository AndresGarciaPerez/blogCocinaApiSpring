package myApp.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthRequest {

        @NotBlank(message = "Campo username obligatorio")
        @Size(min = 3, max = 25, message = "Debe tener un minimo de tres caracteres y un maximo de 25")
        private String username;

        @NotBlank(message = "Campo password obligatorio")
        private String password;

        // getters y setters

}
