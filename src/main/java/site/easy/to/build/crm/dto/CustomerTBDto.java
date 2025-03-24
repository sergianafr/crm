package site.easy.to.build.crm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.easy.to.build.crm.entity.Customer;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTBDto {
     private Integer customerId;
    private String customerName;
    private String customerCountry;
    private BigDecimal totalAmount;
    
    public static CustomerTBDto fromEntity(Customer c){
        return new CustomerTBDto(c.getCustomerId(), c.getName(), c.getCountry(), BigDecimal.ZERO);
    }
}
