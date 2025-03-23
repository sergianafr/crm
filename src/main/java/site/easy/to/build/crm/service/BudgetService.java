package site.easy.to.build.crm.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;

@Service
public class BudgetService {
    @Autowired 
    private BudgetRepository budgetRepository;

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }
    public List<CustomerTBDto> getTotalBudget(){
        return budgetRepository.findTotalBudgetCustomer();
    }
    public BigDecimal getTotalAmount(){
        List<CustomerTBDto> budgets = getTotalBudget();
        BigDecimal total = BigDecimal.ZERO;
        for (CustomerTBDto customerTBDto : budgets) {
            System.out.println(customerTBDto.getTotalAmount());
            total = total.add(customerTBDto.getTotalAmount());
            System.out.println(total);
        }
        return total;
    }
}
