package ar.edu.ubp.das.mascotasrest.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;

/**
 * Traduce los errores que lanzan los procedimientos almacenados (THROW 5000x)
 * y las violaciones de índices únicos a errores de negocio con su código HTTP.
 *
 *   50001 -> 403  usuario sin permiso sobre el refugio / refugio no habilitado
 *   50002 -> 400  especie inexistente
 *   50003 -> 404  publicación inexistente o de otro refugio
 *   50004 -> 400  estado inválido
 *   50005 -> 409  publicación finalizada (no se puede modificar)
 *   50006 -> 409  la publicación ya está en ese estado
 *   2601 / 2627 -> 409  violación de índice único (mascota con publicación vigente)
 */
public final class ErroresBaseDatos {

    private ErroresBaseDatos() {
    }

    public static RuntimeException traducir(DataAccessException e) {
        Throwable causa = e.getMostSpecificCause();
        if (!(causa instanceof SQLException sql)) {
            return e;
        }
        return switch (sql.getErrorCode()) {
            case 50001 -> new NegocioException(HttpStatus.FORBIDDEN, sql.getMessage());
            case 50002, 50004 -> new NegocioException(HttpStatus.BAD_REQUEST, sql.getMessage());
            case 50003 -> new NegocioException(HttpStatus.NOT_FOUND, sql.getMessage());
            case 50005, 50006 -> new NegocioException(HttpStatus.CONFLICT, sql.getMessage());
            case 2601, 2627 -> new NegocioException(HttpStatus.CONFLICT,
                    "La mascota ya posee una publicación de adopción vigente.");
            default -> e;
        };
    }
}
