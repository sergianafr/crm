package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.repository.TicketRepository;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.dto.CustomerDto;
import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.dto.CustomerTEDto;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired private BudgetRepository budgetRepository;
    private final CustomerRepository customerRepository;
    @Autowired private LeadRepository leadRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private LeadService leadService;
    @Autowired private TicketService ticketService;
    
    @Autowired private TicketRepository ticketRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }

    @Override
    public BigDecimal getTotalBudget(int customerId) {
        Customer c = customerRepository.findByCustomerId(customerId);
        if (c == null) {
            return BigDecimal.ZERO;
        }
        List<Budget> budgets = budgetRepository.findByCustomer(c);
        BigDecimal total = BigDecimal.ZERO;
        for (Budget b : budgets) {
            total = total.add(b.getAmount());
        }
        return total;
        
    }
    @Override
    public BigDecimal getTotalBudget(Customer c) {
        if (c == null) {
            return BigDecimal.ZERO;
        }
        List<Budget> budgets = budgetRepository.findByCustomer(c);
        BigDecimal total = BigDecimal.ZERO;
        for (Budget b : budgets) {
            total = total.add(b.getAmount());
        }
        return total;
    }
        
    @Override
    public BigDecimal getTotalExpense(int customerId){
        List<Lead> leads = leadRepository.findByCustomerCustomerId(customerId);
        List<Ticket> tickets = ticketRepository.findByCustomerCustomerId(customerId);
        BigDecimal total = BigDecimal.ZERO;
        for (Lead l : leads) {
            total = total.add(leadService.getTotalExpense(l));
        }
        for (Ticket t : tickets) {
            total = total.add(ticketService.getTotalExpense(t));
        }
        return total;
    }
    @Override
    public BigDecimal getTotalExpense(Customer c){
        List<Lead> leads = leadRepository.findByCustomerCustomerId(c.getCustomerId());
        List<Ticket> tickets = ticketRepository.findByCustomerCustomerId(c.getCustomerId());
        BigDecimal total = BigDecimal.ZERO;
        for (Lead l : leads) {
            total = total.add(leadService.getTotalExpense(l));
        }
        for (Ticket t : tickets) {
            total = total.add(ticketService.getTotalExpense(t));
        }
        return total;
    }
    @Override
    public List<CustomerDto> getAllCustomers() {
        // Récupérer tous les clients depuis la base de données
        List<Customer> customers = customerRepository.findAll();

        // Convertir chaque Customer en CustomerDto
        return customers.stream()
                .map(customer -> {
                    CustomerDto customerDto = CustomerDto.fromEntity(customer);
                    BigDecimal budget = getTotalBudget(customer);
                    BigDecimal expense = getTotalExpense(customer);
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                    symbols.setGroupingSeparator(','); 
                    symbols.setDecimalSeparator('.');
                    DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);                      // Récupérer le montant de la première expense
                    customerDto.setBudget(formatter.format(budget));
                    customerDto.setExpense(formatter.format(expense));
                    return customerDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerTBDto> getExpenseCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerTBDto> ce = new ArrayList<>();
        for (Customer c : customers) {
            CustomerTBDto cet = CustomerTBDto.fromEntity(c);
            cet.setTotalAmount(getTotalExpense(c));
            System.out.println("exp" + getTotalExpense(c));
            ce.add(cet);
        }
        return ce;
    }
    @Override
    public BigDecimal getTotalExpense(){
        List<Customer> customers = customerRepository.findAll();
        BigDecimal ce = BigDecimal.ZERO;
        for (Customer s : customers) {
            ce = ce.add(getTotalExpense(s));
            System.out.println(ce +"expp");
        }
        return ce;
    }
}
