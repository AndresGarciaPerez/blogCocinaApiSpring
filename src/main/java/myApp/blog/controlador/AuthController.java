package myApp.blog.controlador;

import jakarta.validation.Valid;
import myApp.blog.dto.*;
import myApp.blog.modelo.Usuario;
import myApp.blog.repositorio.UsuarioRepositorio;
import myApp.blog.seguridad.jwt.JwtUtils;
import myApp.blog.servicio.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth") //localhost:8080/auth

@CrossOrigin(value = "http://localhost:4200")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@Valid @RequestBody AuthRequest authRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Buscar al usuario desde la base de datos
            Usuario usuario = usuarioRepositorio.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // Generar token confiable con ID y username
            String token = jwtUtils.generarToken(usuario);

            UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuarioDTO);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistroRequest request){
        try {
            Map<String, Object> response = authService.registrarUsuario(request);
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (AuthenticationException e){
            return ResponseEntity.badRequest().body("Error al registrarse");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/actualizar")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(Authentication authentication, @RequestBody ActualizarUsuarioRequest request){
        String usernameActual = authentication.getName();

        Usuario usuarioActualizado = authService.actualizarUsuario(usernameActual, request);
        String nuevoToken = jwtUtils.generarToken(usuarioActualizado);
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioActualizado);

        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuarioResponse);
        response.put("token", nuevoToken);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("isAuthenticated")
    @PutMapping("/actualizar-password")
    public ResponseEntity<Map<String, Object>> actualizarPassword(@RequestBody CambiarPasswordRequest request, Authentication authentication){

        String username = authentication.getName();

        Usuario usuarioActualizado = authService.actualizarPassword(username, request);
        String nuevoToken = jwtUtils.generarToken(usuarioActualizado);
       // UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioActualizado);

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuarioActualizado.getIdUsuario(),
                usuarioActualizado.getUsername(),
                usuarioActualizado.getRole()
        );



        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuarioDTO);
        response.put("token", nuevoToken);

        return ResponseEntity.ok(response);

    }


    @PreAuthorize("isAuthenticated()") //expresion de SpringSecurity lo hace al interceptar el token
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarUsuarioAutenticado(Authentication authentication){
        String username = authentication.getName();

        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByUsername(username);
        if(usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no existe");
        }

        authService.eliminarUsuario(username);
        return ResponseEntity.ok(Collections.singletonMap ("respuesta","Usuario eliminado con exito"));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/perfil")
    public ResponseEntity<UserInfoResponse> obtenerPerfil(Authentication authentication){
        String username = authentication.getName();
        UserInfoResponse perfil = authService.listarPerfilUsuario(username);
        return ResponseEntity.ok(perfil);

    }

}
