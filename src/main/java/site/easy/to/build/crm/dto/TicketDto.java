package site.easy.to.build.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private int ticketId;
    private String subject;
    private String description;
    private String status;
    private String priority;
    private String employeeName;
    private String customerName;
    private String managerName;
    private String createdAt;
    private String expense;
}
