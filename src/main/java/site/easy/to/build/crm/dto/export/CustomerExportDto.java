package site.easy.to.build.crm.dto.export;

import java.util.List;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerExportDto {
    private String email;
    private String name;
    private String phone; 
    private String address;
    private String city;
    private String state;
    private String country;
    private int idUser;
    // private CustomerLoginInfoDto profile;
    private List<BudgetExportDto> budgets;
    private List<TicketExportDto> tickets;
    private List<LeadExportDto> leads;
}
