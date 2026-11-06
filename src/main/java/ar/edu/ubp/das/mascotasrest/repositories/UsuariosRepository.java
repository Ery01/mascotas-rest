package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioLoginBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuariosRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Devuelve el usuario habilitado con ese CUIL y su rol, o null si no existe.
     */
    public UsuarioLoginBE getUsuarioLogin(String cuil) {
        List<UsuarioLoginBE> usuarios = jdbcTemplate.query(
                "exec dbo.get_usuario_login ?",
                new BeanPropertyRowMapper<>(UsuarioLoginBE.class),
                cuil
        );
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}
