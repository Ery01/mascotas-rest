package ar.edu.ubp.das.mascotasrest.BE;

public class LoginResponseBE {

    private String token;
    private String tipoToken;
    private Long expiraEn;
    private UsuarioSesionBE usuario;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public Long getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(Long expiraEn) {
        this.expiraEn = expiraEn;
    }

    public UsuarioSesionBE getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSesionBE usuario) {
        this.usuario = usuario;
    }
}
