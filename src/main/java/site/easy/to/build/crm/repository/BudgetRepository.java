package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public Budget save(Budget budget);
    public List<Budget> findByCustomer(Customer customer);

    @Query("SELECT NEW site.easy.to.build.crm.dto.CustomerTBDto(c.id, c.name, c.country, SUM(b.amount)) " +
           "FROM Budget b JOIN b.customer c " +
           "GROUP BY c.id, c.name, c.country")
    List<CustomerTBDto> findTotalBudgetCustomer();
}
