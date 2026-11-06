package ar.edu.ubp.das.mascotasrest.BE;

import java.time.LocalDateTime;

public class PublicacionBE {

    private Integer nroPublicacion;
    private String nroRegMunicipal;
    private String nombre;
    private String especie;
    private String sexo;
    private Integer edad;
    private String raza;
    private String caracteristicasMascota;
    private String condicionAdopcion;
    private byte[] foto;
    private String tipoFoto;
    private String estadoPublicacion;
    private LocalDateTime fechaPublicacion;

    public Integer getNroPublicacion() {
        return nroPublicacion;
    }

    public void setNroPublicacion(Integer nroPublicacion) {
        this.nroPublicacion = nroPublicacion;
    }

    public String getNroRegMunicipal() {
        return nroRegMunicipal;
    }

    public void setNroRegMunicipal(String nroRegMunicipal) {
        this.nroRegMunicipal = nroRegMunicipal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
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

    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public String toString() {
        return "PublicacionBE{nroPublicacion=" + nroPublicacion + ", nroRegMunicipal=" + nroRegMunicipal
                + ", estadoPublicacion=" + estadoPublicacion + "}";
    }
}
