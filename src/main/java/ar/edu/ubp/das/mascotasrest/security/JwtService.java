package ar.edu.ubp.das.mascotasrest.security;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String ISSUER = "mascotas-cba";

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMinutes = expirationMinutes;
    }

    public long getExpirationSeconds() {
        return expirationMinutes * 60;
    }

    public String generarToken(UsuarioSesionBE usuario) {
        Date ahora = new Date();
        Date vence = new Date(ahora.getTime() + getExpirationSeconds() * 1000);

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(usuario.getCuil())
                .claim("idCiudadano", usuario.getIdCiudadano())
                .claim("apellido", usuario.getApellido())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol())
                .claim("idRefugio", usuario.getIdRefugio())
                .claim("idVeterinaria", usuario.getIdVeterinaria())
                .claim("razonSocial", usuario.getRazonSocial())
                .issuedAt(ahora)
                .expiration(vence)
                .signWith(key)
                .compact();
    }

    /**
     * Valida firma, emisor y vencimiento. Si algo falla devuelve null.
     */
    public UsuarioSesionBE validarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(ISSUER)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UsuarioSesionBE usuario = new UsuarioSesionBE();
            usuario.setCuil(claims.getSubject());
            usuario.setIdCiudadano(claims.get("idCiudadano", Integer.class));
            usuario.setApellido(claims.get("apellido", String.class));
            usuario.setNombre(claims.get("nombre", String.class));
            usuario.setRol(claims.get("rol", String.class));
            usuario.setIdRefugio(claims.get("idRefugio", Integer.class));
            usuario.setIdVeterinaria(claims.get("idVeterinaria", Integer.class));
            usuario.setRazonSocial(claims.get("razonSocial", String.class));
            return usuario;
        }
        catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
