package site.easy.to.build.crm.dto.export;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketExportDto {
    private String subject;
    private String description;
    private String status;
    private String priority;
    private BigDecimal expense;
    private int employeeId;
    private String createdAt;
}
