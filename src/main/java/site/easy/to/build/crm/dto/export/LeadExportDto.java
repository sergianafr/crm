package site.easy.to.build.crm.dto.export;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor @AllArgsConstructor
public class LeadExportDto {
    private String name;
    private String phone;
    private int employeeId;
    private String status;
    private BigDecimal expense;
    private String createdAt;

}
