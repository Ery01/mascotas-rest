package ar.edu.ubp.das.mascotasrest.BE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginDataBE {

    @NotBlank(message = "El CUIL es obligatorio.")
    @Pattern(regexp = "\\d{11}", message = "El CUIL debe tener 11 dígitos, sin guiones.")
    private String cuil;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 64, message = "La contraseña no puede superar los 64 caracteres.")
    private String clave;

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
