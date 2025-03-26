package site.easy.to.build.crm.dto.export;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.easy.to.build.crm.entity.Budget;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetExportDto {
    private BigDecimal amount;
    private int idUser;
    private String createdAt;
}
