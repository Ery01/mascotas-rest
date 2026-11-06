package ar.edu.ubp.das.mascotasrest.BE;

import jakarta.validation.constraints.*;

public class PublicacionDataBE {

    @NotBlank(message = "El nombre de la mascota es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotNull(message = "La especie es obligatoria.")
    private Integer nroValorEspecie;

    @NotBlank(message = "El sexo es obligatorio.")
    @Pattern(regexp = "[MH]", message = "El sexo debe ser M (macho) o H (hembra).")
    private String sexo;

    @Min(value = 0, message = "La edad no puede ser negativa.")
    @Max(value = 40, message = "La edad no puede superar los 40 años.")
    private Integer edadAproximada;

    @Size(max = 100, message = "La raza no puede superar los 100 caracteres.")
    private String raza;

    @NotBlank(message = "La descripción general es obligatoria.")
    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.")
    private String caracteristicasMascota;

    @NotBlank(message = "Las condiciones de adopción son obligatorias.")
    @Size(max = 1000, message = "Las condiciones no pueden superar los 1000 caracteres.")
    private String condicionAdopcion;

    @NotNull(message = "La fotografía es obligatoria.")
    private byte[] foto;

    @NotBlank(message = "El tipo de la fotografía es obligatorio.")
    private String tipoFoto;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNroValorEspecie() {
        return nroValorEspecie;
    }

    public void setNroValorEspecie(Integer nroValorEspecie) {
        this.nroValorEspecie = nroValorEspecie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdadAproximada() {
        return edadAproximada;
    }

    public void setEdadAproximada(Integer edadAproximada) {
        this.edadAproximada = edadAproximada;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getCaracteristicasMascota() {
        return caracteristicasMascota;
    }

    public void setCaracteristicasMascota(String caracteristicasMascota) {
        this.caracteristicasMascota = caracteristicasMascota;
    }

    public String getCondicionAdopcion() {
        return condicionAdopcion;
    }

    public void setCondicionAdopcion(String condicionAdopcion) {
        this.condicionAdopcion = condicionAdopcion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getTipoFoto() {
        return tipoFoto;
    }

    public void setTipoFoto(String tipoFoto) {
        this.tipoFoto = tipoFoto;
    }

    // No se imprime la foto: evita volcar megabytes en el log de auditoría
    @Override
    public String toString() {
        return "PublicacionDataBE{nombre=" + nombre + ", nroValorEspecie=" + nroValorEspecie + ", sexo=" + sexo
                + ", edadAproximada=" + edadAproximada + ", raza=" + raza + ", tipoFoto=" + tipoFoto
                + ", foto=" + (foto == null ? 0 : foto.length) + " bytes}";
    }
}
