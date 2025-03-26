package site.easy.to.build.crm.service.lead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.HistoryExpense;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.generator.GeneratorService;
import site.easy.to.build.crm.utility.Formatter;
import site.easy.to.build.crm.utility.FrontFormatter;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    // @Autowired CustomerService customerS:ervice;
    @Autowired GeneratorService generatorService;
    @Autowired private ExpenseRepository expenseRepository;
    private final LeadRepository leadRepository;

    public LeadServiceImpl(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Override
    public Lead findByLeadId(int id) {
        return leadRepository.findByLeadId(id);
    }

    @Override
    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    @Override
    public List<Lead> findAssignedLeads(int userId) {
        return leadRepository.findByEmployeeId(userId);
    }

    @Override
    public List<Lead> findCreatedLeads(int userId) {
        return leadRepository.findByManagerId(userId);
    }

    @Override
    public Lead findByMeetingId(String meetingId){
        return leadRepository.findByMeetingId(meetingId);
    }
    @Override
    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    @Override
    public void delete(Lead lead) {
        leadRepository.delete(lead);
    }

    @Override
    public List<Lead> getRecentLeadsByEmployee(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Lead> getRecentCustomerLeads(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0,limit);
        return leadRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        leadRepository.deleteAllByCustomer(customer);
    }

    @Override
    public List<Lead> getRecentLeads(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Lead> getCustomerLeads(int customerId) {
        return leadRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return leadRepository.countByEmployeeId(employeeId);
    }
    @Override
    public List<LeadDto> getAllLeads() {
        // Récupérer tous les leads depuis la base de données
        List<Lead> leads = leadRepository.findAll();
    
        // Convertir chaque Lead en LeadDto et ajouter l'expense
        return leads.stream()
                .map(lead -> {
                    LeadDto leadDto = LeadDto.fromEntity(lead); // Convertir Lead en LeadDto
    
                    // Récupérer l'expense associée au lead
                    List<Expense> expenses = expenseRepository.findByLead(lead);
                    if (!expenses.isEmpty()) {
                        BigDecimal expenseAmount = expenses.get(0).getAmount();
                        leadDto.setExpense(FrontFormatter.formatCurrency(expenseAmount)); 
                    } else {
                        leadDto.setExpense("0.0"); 
                    }
    
                    return leadDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countByManagerId(int managerId) {
        return leadRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerId(int customerId) {
        return leadRepository.countByCustomerCustomerId(customerId);
    }
    @Override
    public BigDecimal getTotalExpense(int leadId) {
        Lead l = leadRepository.findByLeadId(leadId);
        if (l == null) {
            return BigDecimal.ZERO;
        }
        List<Expense> expenses = expenseRepository.findByLead(l);
        BigDecimal total = BigDecimal.ZERO;
        for (Expense e : expenses) {
            total = total.add(e.getAmount());
        }
        return total;
    }
    @Override
    public BigDecimal getTotalExpense(Lead l) {
        if (l == null) {
            return BigDecimal.ZERO;
        }
        List<Expense> expenses = expenseRepository.findByLead(l);
        BigDecimal total = BigDecimal.ZERO;
        for (Expense e : expenses) {
            total = total.add(e.getAmount());
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
    @Override
    public Lead instanceAndGenerate(String[] csv, List<Customer> customers, User connected){
        Lead l = new Lead();
        // Customer c = customerService.findInListByMail(csv[0], customers);
        // l.setCustomer(c);
        l.setEmployee(connected);
        l.setManager(connected);
        l.setName(csv[1]);
        l.setPhone(generatorService.generatePhone());
        l.setStatus(csv[3].toLowerCase());
        l.setCreatedAt(generatorService.generateDate(LocalDateTime.of(2025, 02, 01, 1, 1, 1), LocalDateTime.of(2025, 03, 25, 0, 0, 0) ));
        
        HistoryExpense h = new HistoryExpense();
        h.setDateUpdate(l.getCreatedAt());
        h.setUser(connected);
        List<HistoryExpense> historyExpenses = new ArrayList<>();
        historyExpenses.add(h);

        List<Expense> exps = new ArrayList<>();
        Expense expense = new Expense();
        expense.setLead(l);
        expense.setAmount(new BigDecimal(Formatter.formatToDouble(csv[4])));
        expense.setDescriptions(l.getName());
        expense.setUser(connected);
        h.setExpense(expense);
        expense.setHistoryExpenses(historyExpenses);
        
        exps.add(expense);
        l.setExpenses(exps);

        return l;
    }

    @Override
    public List<Lead> instanceAll(List<String[]> csv,  List<Customer> customers, User connected){
        List<Lead> list = new ArrayList<>();
        for (String[] data : csv) {
            Lead t = instanceAndGenerate(data, customers, connected);
            t.setCustomer(findInListByMail(data[0], customers));
            list.add(t);
        }
        return list;
    }
    @Override
    public List<Lead> saveAll(List<Lead> saveAll){
        return leadRepository.saveAll(saveAll);
    }
    // public Expense instanceAndGenerateExpense()
}
