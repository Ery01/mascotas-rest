package ar.edu.ubp.das.mascotasrest.BE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EstadoPublicacionDataBE {

    @NotBlank(message = "El estado es obligatorio.")
    @Pattern(regexp = "Activa|Pausada|Finalizada", message = "El estado debe ser Activa, Pausada o Finalizada.")
    private String estadoPublicacion;

    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    @Override
    public String toString() {
        return "EstadoPublicacionDataBE{estadoPublicacion=" + estadoPublicacion + "}";
    }
}
