package site.easy.to.build.crm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.easy.to.build.crm.entity.Customer;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTEDto {

    private Integer customerId;
    private String customerName;
    private String customerCountry;
    private BigDecimal totalAmount;

    public static CustomerTEDto fromEntity(Customer c){
        return new CustomerTEDto(c.getCustomerId(), c.getName(), c.getCountry(), BigDecimal.ZERO);
    }
}
