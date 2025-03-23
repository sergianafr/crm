package site.easy.to.build.crm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTBDto {
     private Integer customerId;
    private String customerName;
    private String customerCountry;
    private BigDecimal totalAmount;
}
