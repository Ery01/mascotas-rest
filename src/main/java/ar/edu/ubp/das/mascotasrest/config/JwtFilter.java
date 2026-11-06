package ar.edu.ubp.das.mascotasrest.config;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    public static final String ATRIBUTO_USUARIO = "usuario";

    // Rutas que NO requieren token (sí requieren API Key)
    private static final Set<String> RUTAS_PUBLICAS = Set.of("/auth/login");

    @Autowired
    private JwtService jwtService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || RUTAS_PUBLICAS.contains(RespuestaErrorFiltro.rutaRelativa(request));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            RespuestaErrorFiltro.escribir(request, response, HttpStatus.UNAUTHORIZED,
                    "Debe iniciar sesión para acceder a este recurso.");
            return;
        }

        UsuarioSesionBE usuario = jwtService.validarToken(header.substring(7).trim());
        if (usuario == null) {
            RespuestaErrorFiltro.escribir(request, response, HttpStatus.UNAUTHORIZED,
                    "La sesión es inválida o expiró. Vuelva a iniciar sesión.");
            return;
        }

        request.setAttribute(ATRIBUTO_USUARIO, usuario);
        filterChain.doFilter(request, response);
    }
}
