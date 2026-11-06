package ar.edu.ubp.das.mascotasrest.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@Order(1)
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${security.api-key}")
    private String configuredApiKey;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Las solicitudes preflight de CORS (OPTIONS) no traen headers personalizados
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null || !sonIguales(configuredApiKey, apiKey)) {
            RespuestaErrorFiltro.escribir(request, response, HttpStatus.UNAUTHORIZED,
                    "API Key inválida o inexistente.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    // Comparación en tiempo constante (evita deducir la clave midiendo tiempos)
    private boolean sonIguales(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
