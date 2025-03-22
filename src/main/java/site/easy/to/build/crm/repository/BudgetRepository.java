package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public Budget save(Budget budget);
    public List<Budget> findByCustomer(Customer customer);
}
