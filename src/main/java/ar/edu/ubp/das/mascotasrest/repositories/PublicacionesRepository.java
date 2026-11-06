package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionCreadaBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionDataBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PublicacionesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PublicacionBE> getPublicacionesRefugio(Integer idRefugio, String nombre, String estado) {
        return jdbcTemplate.query(
                "exec dbo.get_publicaciones_refugio ?, ?, ?",
                new BeanPropertyRowMapper<>(PublicacionBE.class),
                idRefugio,
                nombre,
                estado
        );
    }

    /**
     * Llama a dbo.ins_publicacion_adopcion (tiene parámetros OUTPUT, por eso SimpleJdbcCall,
     * igual que ins_localidad en el ejemplo de la cátedra).
     */
    public PublicacionCreadaBE insertarPublicacion(Integer idRefugio, Integer idCiudadano, PublicacionDataBE data) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("ins_publicacion_adopcion")
                .declareParameters(
                        new SqlParameter("id_refugio", Types.INTEGER),
                        new SqlParameter("id_ciudadano", Types.INTEGER),
                        new SqlParameter("nombre", Types.VARCHAR),
                        new SqlParameter("nro_valor_especie", Types.SMALLINT),
                        new SqlParameter("sexo", Types.CHAR),
                        new SqlParameter("edad_aproximada", Types.SMALLINT),
                        new SqlParameter("raza", Types.VARCHAR),
                        new SqlParameter("caracteristicas_mascota", Types.VARCHAR),
                        new SqlParameter("condicion_adopcion", Types.VARCHAR),
                        new SqlParameter("foto", Types.VARBINARY),
                        new SqlParameter("tipo_foto", Types.VARCHAR),
                        new SqlOutParameter("nro_reg_municipal", Types.VARCHAR),
                        new SqlOutParameter("nro_publicacion", Types.INTEGER)
                );

        Map<String, Object> params = new HashMap<>();
        params.put("id_refugio", idRefugio);
        params.put("id_ciudadano", idCiudadano);
        params.put("nombre", data.getNombre());
        params.put("nro_valor_especie", data.getNroValorEspecie());
        params.put("sexo", data.getSexo());
        params.put("edad_aproximada", data.getEdadAproximada());
        params.put("raza", data.getRaza());
        params.put("caracteristicas_mascota", data.getCaracteristicasMascota());
        params.put("condicion_adopcion", data.getCondicionAdopcion());
        params.put("foto", data.getFoto());
        params.put("tipo_foto", data.getTipoFoto());

        Map<String, Object> result = jdbcCall.execute(params);

        PublicacionCreadaBE creada = new PublicacionCreadaBE();
        creada.setNroRegMunicipal((String) result.get("nro_reg_municipal"));
        creada.setNroPublicacion((Integer) result.get("nro_publicacion"));
        return creada;
    }

    public void actualizarEstado(Integer nroPublicacion, Integer idRefugio, Integer idCiudadano, String estado) {
        jdbcTemplate.update(
                "exec dbo.upd_estado_publicacion ?, ?, ?, ?",
                nroPublicacion,
                idRefugio,
                idCiudadano,
                estado
        );
    }
}
