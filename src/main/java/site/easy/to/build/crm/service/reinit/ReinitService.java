package site.easy.to.build.crm.service.reinit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReinitService {
     @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void resetDatabase() {
        // Désactiver temporairement la contrainte de clé étrangère
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        
        // Récupérer toutes les tables et les vider dynamiquement
        List<String> tables = jdbcTemplate.queryForList("SHOW TABLES", String.class);
        for (String table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        
        // Réactiver les contraintes de clé étrangère
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
