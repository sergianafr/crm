package site.easy.to.build.crm.service.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.TicketDto;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.HistoryExpense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.TicketRepository;
import site.easy.to.build.crm.service.generator.GeneratorService;
import site.easy.to.build.crm.utility.Formatter;
import site.easy.to.build.crm.utility.FrontFormatter;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private GeneratorService generatorService;
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket findByTicketId(int id) {
        return ticketRepository.findByTicketId(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    @Override
    public List<Ticket> findManagerTickets(int id) {
        return ticketRepository.findByManagerId(id);
    }

    @Override
    public List<Ticket> findEmployeeTickets(int id) {
        return ticketRepository.findByEmployeeId(id);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> findCustomerTickets(int id) {
        return ticketRepository.findByCustomerCustomerId(id);
    }

    @Override
    public List<Ticket> getRecentTickets(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Ticket> getRecentEmployeeTickets(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Ticket> getRecentCustomerTickets(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return ticketRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return ticketRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerCustomerId(int customerId) {
        return ticketRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        ticketRepository.deleteAllByCustomer(customer);
    }
    @Override
    public BigDecimal getTotalExpense(int ticketId) {
        Ticket l = ticketRepository.findByTicketId(ticketId);
        if (l == null) {
            return BigDecimal.ZERO;
        }
        List<Expense> expenses = expenseRepository.findByTicket(l);
        BigDecimal total = BigDecimal.ZERO;
        for (Expense e : expenses) {
            total = total.add(e.getAmount());
        }
        return total;
    }
    @Override
    public BigDecimal getTotalExpense(Ticket l) {
        if (l == null) {
            return BigDecimal.ZERO;
        }
        List<Expense> expenses = expenseRepository.findByTicket(l);
        BigDecimal total = BigDecimal.ZERO;
        for (Expense e : expenses) {
            total = total.add(e.getAmount());
        }
        return total;
    }
    @Override
    public List<TicketDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TicketDto convertToDTO(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        List<Expense> expenses = expenseRepository.findByTicket(ticket);
        if (!expenses.isEmpty()) {
            BigDecimal expenseAmount = expenses.get(0).getAmount();
            
            dto.setExpense(FrontFormatter.formatCurrency(expenseAmount)); 
        } else {
            dto.setExpense("0.0"); 
        }
        dto.setCreatedAt(FrontFormatter.formatLocalDateTime( ticket.getCreatedAt()));

        if (ticket.getEmployee() != null) {
            dto.setEmployeeName(ticket.getEmployee().getUsername());
        }

        // Get customer name
        if (ticket.getCustomer() != null) {
            dto.setCustomerName(ticket.getCustomer().getName());
        }

        // Get manager name
        if (ticket.getManager() != null) {
            dto.setManagerName(ticket.getManager().getUsername());
        }

        return dto;
    }
    
    @Override
    public Ticket instanceAndGenerate(String[] csv, List<Customer> customers, User connected){
        Ticket t = new Ticket();
        t.setManager(connected);
        t.setEmployee(connected);
        t.setStatus(csv[3].toLowerCase());
        t.setSubject(csv[1]);
        t.setPriority(generatorService.generatePriority());
        t.setCreatedAt(generatorService.generateDate(LocalDateTime.of(2025, 02, 01, 1, 1, 1), LocalDateTime.of(2025, 03, 25, 0, 0, 0) ));
        t.setDescription(generatorService.generateLoremIpsum());

        HistoryExpense h = new HistoryExpense();
        h.setDateUpdate(t.getCreatedAt());
        h.setUser(connected);
        List<HistoryExpense> historyExpenses = new ArrayList<>();
        historyExpenses.add(h);

        Expense expense = new Expense();
        expense.setTicket(t);
        expense.setAmount(new BigDecimal(Formatter.formatToDouble(csv[4])));
        expense.setDescriptions(t.getSubject());
        expense.setUser(connected);
        h.setExpense(expense);
        expense.setHistoryExpenses(historyExpenses);

        List<Expense> exps = new ArrayList<>();
        exps.add(expense);
        t.setExpenses(exps);

        return t;
    }
    public Customer findInListByMail(String email, List<Customer> customers){
        if (customers == null || customers.isEmpty()) {
            System.out.println("null ");
            return null;
        }
        
        return customers.stream()
                .filter(info -> email != null && email.equals(info.getEmail()))
                .findFirst()
                .orElse(null);
    }
    @Override
    public List<Ticket> instanceAll(List<String[]> csv,  List<Customer> customers, User connected){
        List<Ticket> list = new ArrayList<>();
        for (String[] data : csv) {
            Ticket t = instanceAndGenerate(data, customers, connected);
            System.out.println(findInListByMail(data[0], customers)+" "+data[0]);
            t.setCustomer(findInListByMail(data[0], customers));
            list.add(t);
        }
        return list;
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> saveAll){
        return ticketRepository.saveAll(saveAll);
    }
}
