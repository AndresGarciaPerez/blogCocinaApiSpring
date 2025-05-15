package myApp.blog.dto;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String passwordActual;
    private String nuevaPassword;
}
