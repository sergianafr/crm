package site.easy.to.build.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.repository.TicketRepository;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Service
public class DashboardService {
    @Autowired CustomerRepository customerRepository;
    @Autowired TicketRepository ticketRepository;
    @Autowired LeadRepository leadRepository;
    @Autowired LeadService leadService;
    @Autowired TicketService ticketService;
    @Autowired CustomerService customerService;

    
}
