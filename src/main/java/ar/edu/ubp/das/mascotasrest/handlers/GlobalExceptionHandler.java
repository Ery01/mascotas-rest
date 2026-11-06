package ar.edu.ubp.das.mascotasrest.handlers;

import ar.edu.ubp.das.mascotasrest.BE.ErrorBE;
import ar.edu.ubp.das.mascotasrest.exceptions.NegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

/**
 * Convierte cualquier excepción en una respuesta JSON con el formato ErrorBE
 * y el código HTTP correspondiente. Nunca expone detalles internos (SQL, stack trace).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorBE> negocio(NegocioException e, HttpServletRequest request) {
        return crear(e.getStatus(), e.getMessage(), request);
    }

    // Falla de @Valid: se informa el primer mensaje de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBE> validacion(MethodArgumentNotValidException e, HttpServletRequest request) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> f.getDefaultMessage())
                .orElse("Datos inválidos.");
        return crear(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorBE> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest request) {
        return crear(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON válido.", request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorBE> tipoInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return crear(HttpStatus.BAD_REQUEST, "El parámetro '" + e.getName() + "' tiene un formato inválido.", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorBE> metodoNoSoportado(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return crear(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no permitido para este recurso.", request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorBE> noEncontrado(NoResourceFoundException e, HttpServletRequest request) {
        return crear(HttpStatus.NOT_FOUND, "Recurso inexistente.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBE> general(Exception e, HttpServletRequest request) {
        return crear(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Intente nuevamente más tarde.", request);
    }

    private ResponseEntity<ErrorBE> crear(HttpStatus status, String mensaje, HttpServletRequest request) {
        ErrorBE error = new ErrorBE();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(mensaje);
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
