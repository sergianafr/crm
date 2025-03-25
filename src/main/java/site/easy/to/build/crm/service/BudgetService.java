package site.easy.to.build.crm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.generator.GeneratorService;
import site.easy.to.build.crm.utility.Error;
import site.easy.to.build.crm.utility.Formatter;
import site.easy.to.build.crm.utility.Validate;

@Service
public class BudgetService {
    @Autowired 
    private BudgetRepository budgetRepository;
    @Autowired GeneratorService generatorService;

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
    public Customer findInListByMail(String email, List<Customer> customers){
        if (customers == null || customers.isEmpty()) {
            return null;
        }
        
        return customers.stream()
                .filter(info -> email != null && email.equals(info.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public Budget instanceAndGenerate(String[] data, List<Customer> customers, User connected){
        Budget b = new Budget();
        b.setDescriptions(generatorService.generateLoremIpsum());
        System.out.println(data[0]);
        Customer cu = findInListByMail(data[0], customers);
        b.setCreatedAt(cu.getCreatedAt());
        b.setCustomer(cu);
        b.setAmount(new BigDecimal(Formatter.formatToDouble(data[1])));
        b.setUser(connected);
        return b;
    }

    public List<Budget> instanceAll(List<String[]> csv, List<Customer>customers, User connected){
        List<Budget> bs = new ArrayList<>();
        for (int i = 1; i < csv.size(); i++) {
            String[] data = csv.get(i);
            bs.add(instanceAndGenerate(data, customers, connected));
        }
        return bs;
    }

    public List<site.easy.to.build.crm.utility.Error> checkBudgetError(List<String[]>csv, List<String[]> csvCustomer){
        List<site.easy.to.build.crm.utility.Error> errors = new ArrayList<>();
        for (int i = 1; i < csv.size(); i++) {
            String[] data = csv.get(i);
            if(!Validate.containsValue(csvCustomer, 0, data[0])){
                errors.add(new Error("Budget", "The customer does not exist", i, 1));
            }
            if(!Validate.checkAmount(Formatter.formatToDouble(data[1]))){
                errors.add(new Error("Budget", "Amount value cannot be negative.", i, 2));
            }
        }
        return errors;
    }

    public List<Budget> saveImport(List<String[]> csv, List<Customer> customers, User connected){
        List<Budget> list = instanceAll(csv, customers, connected);
        return budgetRepository.saveAll(list);
    }

}
