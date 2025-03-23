package site.easy.to.build.crm.service.customer;

import org.checkerframework.checker.units.qual.C;

import site.easy.to.build.crm.dto.CustomerDto;
import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.dto.CustomerTEDto;
import site.easy.to.build.crm.entity.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    public List<CustomerTBDto> getExpenseCustomers();
    public BigDecimal getTotalExpense();
    public Customer findByCustomerId(int customerId);
    public List<CustomerDto> getAllCustomers();
    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);
    public BigDecimal getTotalBudget(int customerId);
    public BigDecimal getTotalBudget(Customer c);

    public BigDecimal getTotalExpense(int customerId);
    public BigDecimal getTotalExpense(Customer c);

}
