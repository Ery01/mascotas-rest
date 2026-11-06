package ar.edu.ubp.das.mascotasrest.services;

import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionCreadaBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionDataBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.exceptions.ErroresBaseDatos;
import ar.edu.ubp.das.mascotasrest.exceptions.NegocioException;
import ar.edu.ubp.das.mascotasrest.repositories.PublicacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PublicacionesService {

    private static final Set<String> ESTADOS = Set.of("Activa", "Pausada", "Finalizada");

    @Autowired
    private PublicacionesRepository repository;

    @Value("${publicaciones.foto.max-bytes}")
    private int fotoMaxBytes;

    // SOA: ListarPublicacionesRefugio()
    public List<PublicacionBE> listar(UsuarioSesionBE usuario, String nombre, String estado) {
        if (estado != null && !estado.isBlank() && !ESTADOS.contains(estado)) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El estado debe ser Activa, Pausada o Finalizada.");
        }
        return repository.getPublicacionesRefugio(usuario.getIdRefugio(), nombre, estado);
    }

    // SOA: RegistrarPublicacionAdopcion() (+ RegistrarMascota() del servicio 1)
    @Transactional
    public PublicacionCreadaBE registrar(UsuarioSesionBE usuario, PublicacionDataBE data) {
        validarFoto(data.getFoto(), data.getTipoFoto());
        try {
            return repository.insertarPublicacion(usuario.getIdRefugio(), usuario.getIdCiudadano(), data);
        }
        catch (DataAccessException e) {
            throw ErroresBaseDatos.traducir(e);
        }
    }

    // SOA: CambiarEstadoPublicacion()
    @Transactional
    public void cambiarEstado(UsuarioSesionBE usuario, Integer nroPublicacion, String estado) {
        try {
            repository.actualizarEstado(nroPublicacion, usuario.getIdRefugio(), usuario.getIdCiudadano(), estado);
        }
        catch (DataAccessException e) {
            throw ErroresBaseDatos.traducir(e);
        }
    }

    /**
     * Valida tamaño y que el contenido sea realmente una imagen del tipo informado
     * (se revisan los primeros bytes del archivo, no solo la extensión).
     */
    private void validarFoto(byte[] foto, String tipo) {
        if (foto == null || foto.length == 0) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "La fotografía es obligatoria.");
        }
        if (foto.length > fotoMaxBytes) {
            throw new NegocioException(HttpStatus.BAD_REQUEST,
                    "La fotografía no puede superar los " + (fotoMaxBytes / 1024 / 1024) + " MB.");
        }
        boolean valida = switch (tipo) {
            case "image/jpeg" -> empiezaCon(foto, 0xFF, 0xD8, 0xFF);
            case "image/png" -> empiezaCon(foto, 0x89, 0x50, 0x4E, 0x47);
            case "image/webp" -> foto.length > 12
                    && empiezaCon(foto, 0x52, 0x49, 0x46, 0x46)          // RIFF
                    && foto[8] == 0x57 && foto[9] == 0x45 && foto[10] == 0x42 && foto[11] == 0x50; // WEBP
            default -> false;
        };
        if (!valida) {
            throw new NegocioException(HttpStatus.BAD_REQUEST,
                    "La fotografía debe ser una imagen JPG, PNG o WEBP válida.");
        }
    }

    private boolean empiezaCon(byte[] datos, int... firma) {
        if (datos.length < firma.length) {
            return false;
        }
        for (int i = 0; i < firma.length; i++) {
            if ((datos[i] & 0xFF) != firma[i]) {
                return false;
            }
        }
        return true;
    }
}
