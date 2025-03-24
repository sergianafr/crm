package site.easy.to.build.crm.controller.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.easy.to.build.crm.config.JacksonConfig;
import site.easy.to.build.crm.dto.CustomerDto;
import site.easy.to.build.crm.dto.CustomerTBDto;
import site.easy.to.build.crm.dto.DashboardData;
import site.easy.to.build.crm.dto.Evolution;
import site.easy.to.build.crm.dto.ExpenseRequest;
import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.dto.TicketDto;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.DashboardService;
import site.easy.to.build.crm.service.EvolutionService;
import site.easy.to.build.crm.service.ExpenseService;
import site.easy.to.build.crm.service.RateService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.utility.FrontFormatter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/dashboards")
public class DashboardApi {
    @Autowired BudgetService budgetService;
    @Autowired LeadService leadService;
    @Autowired TicketService ticketService;
    @Autowired CustomerService customerService;
    @Autowired UserService userService;
    @Autowired AuthenticationUtils authenticationUtils;
    @Autowired ExpenseService expenseService;
    @Autowired EvolutionService evolutionService;
    @Autowired RateService rateService;
    private final static ObjectMapper mapper = JacksonConfig.objectMapper();

    @GetMapping("/o")
    @CrossOrigin(origins = "http://localhost:5174")
    public ResponseEntity<DashboardData> index() {
        // System.out.println("DashboardApi.index()");
        try {
            List<Customer> customers = customerService.findAll();
            List<Ticket> tickets = ticketService.findAll();
            List<Lead> leads = leadService.findAll();
            int nbCustomers = customers.size();
            int nbTickets = tickets.size();
            int nbLeads = leads.size();

            DashboardData dashboardData = new DashboardData( nbCustomers, nbTickets, nbLeads);

            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des données du tableau de bord");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null); 
        }
    }

        
    @GetMapping("/")
    public String get() {
        System.out.println("DashboardApi.index()");
        ObjectMapper objectMapper = mapper;
        
        try {
            List<Customer> customers = customerService.findAll();
            List<Ticket> tickets = ticketService.findAll();
            List<Lead> leads = leadService.findAll();
            BigDecimal totalB = budgetService.getTotalAmount();
            int nbCustomers = customers.size();
            int nbTickets = tickets.size();
            int nbLeads = leads.size();
            String totalBudgetFormat = FrontFormatter.formatCurrency(totalB);
            List<CustomerTBDto> customerBudget = budgetService.getTotalBudget();
            BigDecimal totalExpense = customerService.getTotalExpense();
            String totalExpenseFormat = FrontFormatter.formatCurrency(totalExpense);
            List<CustomerTBDto> customerExpense = customerService.getExpenseCustomers(); 
            BigDecimal expenseTicket = expenseService.getTotalTicket();
            String expenseTicketFormat = FrontFormatter.formatCurrency(expenseTicket);
            BigDecimal expenseLead = expenseService.getTotalLead();
            String expenseLeadFormat = FrontFormatter.formatCurrency(expenseLead);
            List<Evolution> evolutionByDate = evolutionService.getEvolutionData();
            Double actualRate = rateService.findMax().getRate();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("nbCustomers", nbCustomers);
            response.put("nbTickets", nbTickets);
            response.put("nbLeads", nbLeads);
            response.put("totalBudget", totalB);
            response.put("totalBudgetFormat", totalBudgetFormat);
            response.put("customerBudget", customerBudget);
            response.put("totalExpense", totalExpense);
            response.put("totalExpenseFormat", totalExpenseFormat);
            response.put("customerExpense", customerExpense);
            response.put("expenseTicket", expenseTicket);
            response.put("expenseTicketFormat", expenseTicketFormat);
            response.put("expenseLead", expenseLead);
            response.put("expenseLeadFormat", expenseLeadFormat);
            response.put("evolutionByDate", evolutionByDate);
            response.put("rate", actualRate);
            return objectMapper.writeValueAsString(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());

            try {
                return objectMapper.writeValueAsString(errorResponse);
            } catch (Exception jsonException) {
                return "{\"success\":false, \"error\":\"Unknown error\"}";
            }
        }
    }
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> getTickets() {
        try {
            List<TicketDto> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(tickets);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }
    @GetMapping("/leads")
    public ResponseEntity<List<LeadDto>> getLeads() {
        try {
            List<LeadDto> tickets = leadService.getAllLeads();
            return ResponseEntity.ok(tickets);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        try {
            List<CustomerDto> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
            
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }

    @PostMapping("/leads/update-expense")
    public String updateExpenseLead(@RequestBody ExpenseRequest entity) {
        System.out.println(entity.getAmount());
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User connected = userService.findById(authenticationUtils.getLoggedInUserId(authentication));
            System.out.println(connected.getId());
            expenseService.updateLead(entity, connected);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        }
        
    }
    @PostMapping("/leads/delete/{id}")
    public String deleteLead(@PathVariable("id") int id) {
        try{
            Lead lead = leadService.findByLeadId(id);
            leadService.delete(lead);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        }
    }

    @PostMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable("id") int id) {
        try{
            Ticket lead = ticketService.findByTicketId(id);
            ticketService.delete(lead);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        }
    }
    
    @PostMapping("/tickets/update-expense")
    public String updateExpenseTicket(@RequestBody ExpenseRequest entity) {
        System.out.println(entity.getAmount());
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User connected = userService.findById(authenticationUtils.getLoggedInUserId(authentication));
            System.out.println(connected.getId());
            expenseService.updateTicket(entity, connected);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        } 
    }
    @PostMapping("/leads/delete-expense")
    public String deleteExpenseLead(@RequestBody ExpenseRequest entity) {
        System.out.println(entity.getAmount());
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User connected = userService.findById(authenticationUtils.getLoggedInUserId(authentication));
            System.out.println(connected.getId());
            expenseService.updateLead(entity, connected);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        }
        
    }
    @PostMapping("/tickets/delete-expense")
    public String deleteTicketLead(@RequestBody ExpenseRequest entity) {
        System.out.println(entity.getAmount());
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User connected = userService.findById(authenticationUtils.getLoggedInUserId(authentication));
            System.out.println(connected.getId());
            expenseService.updateTicket(entity, connected);
            return "{\"success\": true}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"success\": false}";
        } 
    }
    
    
}
