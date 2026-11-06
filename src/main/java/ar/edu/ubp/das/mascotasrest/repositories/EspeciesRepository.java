package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EspeciesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EspecieBE> getEspecies() {
        return jdbcTemplate.query(
                "exec dbo.get_especies",
                new BeanPropertyRowMapper<>(EspecieBE.class)
        );
    }
}
