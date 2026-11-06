package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.*;
import ar.edu.ubp.das.mascotasrest.config.JwtFilter;
import ar.edu.ubp.das.mascotasrest.security.Roles;
import ar.edu.ubp.das.mascotasrest.security.RolesPermitidos;
import ar.edu.ubp.das.mascotasrest.services.PublicacionesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicaciones")
@RolesPermitidos(Roles.REFUGIO)
public class PublicacionesResource {

    @Autowired
    private PublicacionesService service;

    @GetMapping
    public ResponseEntity<List<PublicacionBE>> getPublicaciones(
            @RequestAttribute(JwtFilter.ATRIBUTO_USUARIO) UsuarioSesionBE usuario,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(service.listar(usuario, nombre, estado));
    }

    @PostMapping
    public ResponseEntity<PublicacionCreadaBE> insertarPublicacion(
            @RequestAttribute(JwtFilter.ATRIBUTO_USUARIO) UsuarioSesionBE usuario,
            @Valid @RequestBody PublicacionDataBE data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(usuario, data));
    }

    @PatchMapping("/{nroPublicacion}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @RequestAttribute(JwtFilter.ATRIBUTO_USUARIO) UsuarioSesionBE usuario,
            @PathVariable Integer nroPublicacion,
            @Valid @RequestBody EstadoPublicacionDataBE data) {
        service.cambiarEstado(usuario, nroPublicacion, data.getEstadoPublicacion());
        return ResponseEntity.noContent().build();
    }
}
