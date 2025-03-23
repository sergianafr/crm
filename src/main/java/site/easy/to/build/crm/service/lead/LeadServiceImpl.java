package site.easy.to.build.crm.service.lead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.entity.Lead;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

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
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
                        symbols.setGroupingSeparator(','); 
                        symbols.setDecimalSeparator('.');
                        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);                      // Récupérer le montant de la première expense
                        leadDto.setExpense(formatter.format(expenseAmount)); // Ajouter l'expense au LeadDto
                    } else {
                        leadDto.setExpense("0.0"); // Si aucune expense n'est trouvée, définir l'expense à 0
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
}
