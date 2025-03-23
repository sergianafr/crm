package site.easy.to.build.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.easy.to.build.crm.entity.Customer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String country;
    private String budget;
    private String expense;

    public static CustomerDto fromEntity(Customer customer) {
        return new CustomerDto(
            customer.getCustomerId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getCountry(),"0", "0"
        );
    }
}
