package ar.edu.ubp.das.mascotasrest.services;

import ar.edu.ubp.das.mascotasrest.BE.LoginDataBE;
import ar.edu.ubp.das.mascotasrest.BE.LoginResponseBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioLoginBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.exceptions.NegocioException;
import ar.edu.ubp.das.mascotasrest.repositories.UsuariosRepository;
import ar.edu.ubp.das.mascotasrest.security.JwtService;
import ar.edu.ubp.das.mascotasrest.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private static final String CREDENCIALES_INVALIDAS = "CUIL o contraseña incorrectos.";

    // Roles que pueden autenticarse con CUIL + contraseña
    private static final Set<String> ROLES_CON_CLAVE = Set.of(Roles.REFUGIO, Roles.VETERINARIA);

    // Hash BCrypt de referencia: se usa cuando el CUIL no existe para que la
    // respuesta tarde lo mismo y no se pueda saber qué CUIL están registrados.
    private static final String HASH_FICTICIO = "$2a$10$uc4FbAJgLL3w4Z1wvkIcJO0qczkB6WcrKlKdnZLkjQ8qxXDBjbUC6";

    @Autowired
    private UsuariosRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public LoginResponseBE login(LoginDataBE data) {
        UsuarioLoginBE usuario = repository.getUsuarioLogin(data.getCuil());

        String hash = (usuario != null && usuario.getClave() != null) ? usuario.getClave() : HASH_FICTICIO;
        boolean claveCorrecta = passwordEncoder.matches(data.getClave(), hash);

        if (usuario == null || usuario.getClave() == null || !claveCorrecta
                || !ROLES_CON_CLAVE.contains(usuario.getRol())) {
            throw new NegocioException(HttpStatus.UNAUTHORIZED, CREDENCIALES_INVALIDAS);
        }

        UsuarioSesionBE sesion = new UsuarioSesionBE();
        sesion.setIdCiudadano(usuario.getIdCiudadano());
        sesion.setCuil(usuario.getCuil());
        sesion.setApellido(usuario.getApellido());
        sesion.setNombre(usuario.getNombre());
        sesion.setRol(usuario.getRol());
        sesion.setIdRefugio(usuario.getIdRefugio());
        sesion.setIdVeterinaria(usuario.getIdVeterinaria());
        sesion.setRazonSocial(usuario.getRazonSocial());

        LoginResponseBE respuesta = new LoginResponseBE();
        respuesta.setToken(jwtService.generarToken(sesion));
        respuesta.setTipoToken("Bearer");
        respuesta.setExpiraEn(jwtService.getExpirationSeconds());
        respuesta.setUsuario(sesion);
        return respuesta;
    }
}
