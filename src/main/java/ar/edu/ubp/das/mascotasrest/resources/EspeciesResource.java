package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import ar.edu.ubp.das.mascotasrest.security.Roles;
import ar.edu.ubp.das.mascotasrest.security.RolesPermitidos;
import ar.edu.ubp.das.mascotasrest.services.EspeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/especies")
@RolesPermitidos({Roles.REFUGIO, Roles.VETERINARIA})
public class EspeciesResource {

    @Autowired
    private EspeciesService service;

    @GetMapping
    public ResponseEntity<List<EspecieBE>> getEspecies() {
        return ResponseEntity.ok(service.getEspecies());
    }
}
