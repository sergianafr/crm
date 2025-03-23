package site.easy.to.build.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.*;
import site.easy.to.build.crm.entity.Lead;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadDto {
    private int leadId;
    private String name;
    private String status;
    private String phone;
    private String managerName; // Nom du manager
    private String employeeName; // Nom de l'employé
    private String customerName; // Nom du client
    private String createdAt; // Date de création en String
    private String expense;

    // Méthode pour convertir une entité Lead en LeadDto
    public static LeadDto fromEntity(Lead lead) {
        LeadDto dto = new LeadDto();
        dto.setLeadId(lead.getLeadId());
        dto.setName(lead.getName());
        dto.setStatus(lead.getStatus());
        dto.setPhone(lead.getPhone());

        // Convertir LocalDateTime en String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dto.setCreatedAt(lead.getCreatedAt().format(formatter));

        // Récupérer les noms des relations
        if (lead.getManager() != null) {
            dto.setManagerName(lead.getManager().getUsername());
        }
        if (lead.getEmployee() != null) {
            dto.setEmployeeName(lead.getEmployee().getUsername());
        }
        if (lead.getCustomer() != null) {
            dto.setCustomerName(lead.getCustomer().getName());
        }

        return dto;
    }
}