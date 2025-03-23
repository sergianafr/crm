package site.easy.to.build.crm.controller.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.easy.to.build.crm.config.JacksonConfig;
import site.easy.to.build.crm.dto.CustomerDto;
import site.easy.to.build.crm.dto.DashboardData;
import site.easy.to.build.crm.dto.LeadDto;
import site.easy.to.build.crm.dto.TicketDto;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.DashboardService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/dashboards")
public class DashboardApi {
    @Autowired LeadService leadService;
    @Autowired TicketService ticketService;
    @Autowired CustomerService customerService;
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

            // Créer l'objet DashboardData
            DashboardData dashboardData = new DashboardData( nbCustomers, nbTickets, nbLeads);

            // Retourner la réponse avec un statut HTTP 200 (OK)
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des données du tableau de bord");
            // Retourner une réponse d'erreur avec un statut HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null); // ou un objet d'erreur personnalisé
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

            int nbCustomers = customers.size();
            int nbTickets = tickets.size();
            int nbLeads = leads.size();

            // Créer la réponse en tant que HashMap
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("nbCustomers", nbCustomers);
            response.put("nbTickets", nbTickets);
            response.put("nbLeads", nbLeads);
            response.put("dati", LocalDateTime.now());

            // Convertir la Map en JSON string et retourner directement la chaîne
            return objectMapper.writeValueAsString(response);

        } catch (Exception e) {
            e.printStackTrace();

            // En cas d'erreur, renvoyer un JSON sous forme de String
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
            // Retourner une réponse d'erreur avec un statut HTTP 500 (Internal Server Error)
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
            // Retourner une réponse d'erreur avec un statut HTTP 500 (Internal Server Error)
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
    
}
