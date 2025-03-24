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
    // Requête pour récupérer les données de Budget
    String budgetQuery = """
        SELECT 
            0.0 AS amountExpense,
            SUM(b.amount) AS amountBudget,
            DATE_FORMAT(b.created_At, '%Y-%m-%d %H:%i:%s') AS date
        FROM Budget b
        GROUP BY DATE_FORMAT(b.created_At, '%Y-%m-%d %H:%i:%s')
    """;

    // Requête pour récupérer les données de HistoryExpense
    String expenseQuery = """
        SELECT 
            SUM(he.amount) AS amountExpense,
            0.0 AS amountBudget,
            DATE_FORMAT(he.date_Update, '%Y-%m-%d %H:%i:%s') AS date
        FROM History_Expense he
        GROUP BY DATE_FORMAT(he.date_Update, '%Y-%m-%d %H:%i:%s')
    """;

    // Exécution des requêtes et mapping manuel
    List<Evolution> budgetResults = mapQueryResults(entityManager.createNativeQuery(budgetQuery).getResultList());
    List<Evolution> expenseResults = mapQueryResults(entityManager.createNativeQuery(expenseQuery).getResultList());

    // Fusion des résultats dans une Map
    Map<String, Evolution> evolutionMap = new HashMap<>();

    // Ajout des résultats de Budget
    for (Evolution evo : budgetResults) {
        evolutionMap.put(evo.getDate(), evo);
    }

    // Ajout des résultats de Expense
    for (Evolution evo : expenseResults) {
        evolutionMap.merge(evo.getDate(), evo, (existing, newEvo) -> {
            existing.setAmountExpense(newEvo.getAmountExpense());
            return existing;
        });
    }

    // Conversion de la Map en List
    return new ArrayList<>(evolutionMap.values());
}

// Helper method to manually map query results to Evolution objects
private List<Evolution> mapQueryResults(List<Object[]> results) {
    List<Evolution> evolutionList = new ArrayList<>();
    for (Object[] row : results) {
        BigDecimal amountExpense = (BigDecimal) row[0]; // amountExpense
        BigDecimal amountBudget = (BigDecimal) row[1];  // amountBudget
        String date = (String) row[2];                 // date
        evolutionList.add(new Evolution(amountExpense, amountBudget, date));
    }
    return evolutionList;
}
    
}


