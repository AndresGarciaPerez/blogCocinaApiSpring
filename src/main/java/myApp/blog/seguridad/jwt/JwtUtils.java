package myApp.blog.seguridad.jwt;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import myApp.blog.modelo.Usuario;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "mi_clave_super_creada_generada_para_jwt";
    private static final long EXPIRATION = 3600000; //1 hora


    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generarToken(Usuario usuario){

        Map<String, Object> claims = new HashMap<>();
        claims.put("idUsuario", usuario.getIdUsuario());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean esTokenValido(String token){
        try{
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e ){
            return false;
        }
    }

}
