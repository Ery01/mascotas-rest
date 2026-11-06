package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.LoginDataBE;
import ar.edu.ubp.das.mascotasrest.BE.LoginResponseBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.config.JwtFilter;
import ar.edu.ubp.das.mascotasrest.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBE> login(@Valid @RequestBody LoginDataBE data) {
        return ResponseEntity.ok(service.login(data));
    }

    /**
     * Devuelve el usuario del token. Angular lo usa al recargar la página
     * para recuperar el contexto de la sesión.
     */
    @GetMapping("/usuario")
    public ResponseEntity<UsuarioSesionBE> getUsuario(
            @RequestAttribute(JwtFilter.ATRIBUTO_USUARIO) UsuarioSesionBE usuario) {
        return ResponseEntity.ok(usuario);
    }}
