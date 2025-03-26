package site.easy.to.build.crm.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evolution {
    private BigDecimal amountExpense;
    private Long amountBudget;
    private String date;
}
