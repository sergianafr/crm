package site.easy.to.build.crm.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import site.easy.to.build.crm.dto.Evolution;

@Service
public class EvolutionService {
    @Autowired
    private EntityManager entityManager;

    public List<Evolution> getEvolutionData() {
        // Requête pour récupérer uniquement les données de Budget
        String query = """
            SELECT 
                0.0 AS amountExpense, 
                SUM(b.amount) AS amountBudget,
                DATE_FORMAT(b.created_At, '%Y-%m-%d %H:%i:%s') AS date
            FROM Budget b
            GROUP BY DATE_FORMAT(b.created_At, '%Y-%m-%d %H:%i:%s')
            ORDER BY date ASC
        """;

        // Exécution de la requête et mapping des résultats
        List<Evolution> results = mapQueryResults(entityManager.createNativeQuery(query).getResultList());

        return results;
    }

    // Méthode helper pour mapper les résultats
    private List<Evolution> mapQueryResults(List<Object[]> results) {
        List<Evolution> evolutionList = new ArrayList<>();
        for (Object[] row : results) {
            BigDecimal amountExpense = (BigDecimal) row[0]; // Toujours 0.0
            BigDecimal amountBudget = (BigDecimal) row[1];  // Montant du budget
            String date = (String) row[2];                 // Date
            evolutionList.add(new Evolution(amountExpense, amountBudget, date));
        }
        return evolutionList;
    }
}


