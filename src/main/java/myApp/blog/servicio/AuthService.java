package myApp.blog.servicio;

import myApp.blog.dto.*;
import myApp.blog.mensajeExcepcion.RecursoNoEncontrado;
import myApp.blog.modelo.Usuario;
import myApp.blog.repositorio.UsuarioRepositorio;
import myApp.blog.seguridad.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> registrarUsuario(RegistroRequest request){
        if(usuarioRepositorio.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya esta en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole(request.getRole());
        Usuario usuarioRegistrado = usuarioRepositorio.save(usuario);

        String token = jwtUtils.generarToken(usuario);

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("idUsuario", usuarioRegistrado.getIdUsuario());
        usuarioMap.put("username", usuarioRegistrado.getUsername());
        usuarioMap.put("role", usuarioRegistrado.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", usuarioMap);

        return response;
    }

    public Usuario actualizarUsuario(String username, ActualizarUsuarioRequest request){
        Usuario usuario = usuarioRepositorio.findByUsername(username).
                orElseThrow( () -> new RecursoNoEncontrado("Usuario no encontrado"));
        usuario.setUsername(request.getUsername());
        usuario.setRole(request.getRole());
        return usuarioRepositorio.save(usuario);

    }

    public Usuario actualizarPassword(String username, CambiarPasswordRequest request){
        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));

        if(!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword() )){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña actual no coincide");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNuevaPassword()));
        return usuarioRepositorio.save(usuario);
    }


    public void eliminarUsuario(String username){
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findByUsername(username);
        usuarioOptional.ifPresent(usuarioRepositorio::delete);
    }

    public UserInfoResponse listarPerfilUsuario(String username){
        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("Usuario no encontrado"));

        return new UserInfoResponse(usuario.getUsername(), usuario.getRole());
    }


}
