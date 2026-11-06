package ar.edu.ubp.das.mascotasrest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;

public class RespuestaErrorFiltro {

    private RespuestaErrorFiltro() {
    }

    static void escribir(HttpServletRequest request, HttpServletResponse response,
                         HttpStatus status, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"timestamp\":\"" + LocalDateTime.now() + "\"," +
                        "\"status\":" + status.value() + "," +
                        "\"error\":\"" + status.getReasonPhrase() + "\"," +
                        "\"message\":\"" + mensaje.replace("\"", "\\\"") + "\"," +
                        "\"path\":\"" + request.getRequestURI() + "\"}"
        );
    }

    /**
     * Ruta sin el context-path (/api/v1). Ej: /api/v1/auth/login -> /auth/login
     */
    static String rutaRelativa(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}
