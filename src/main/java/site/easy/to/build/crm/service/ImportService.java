package site.easy.to.build.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.utility.Error;
import site.easy.to.build.crm.utility.Formatter;
import site.easy.to.build.crm.utility.ImportTemplate;
import site.easy.to.build.crm.utility.Validate;

@Service
public class ImportService {
    @Autowired LeadService leadService;
    @Autowired TicketService ticketService;
    @Autowired CustomerService customerService;
    @Autowired BudgetService budgetService;

    @Transactional
    public List<site.easy.to.build.crm.utility.Error> checkErrorTicketLead(List<String[]> csv, List<String[]> csvCustomers){
        List<site.easy.to.build.crm.utility.Error> errors = new ArrayList<>();
        for (int i = 1; i < csv.size(); i++) {
            if(!Validate.containsValue(csvCustomers, 0, csv.get(i)[0])){
                errors.add(new Error("Lead-and-ticket", "The customer does not exist.", i, 1));
            } if(!Validate.checkType(csv.get(i)[2])){
                errors.add(new Error("Lead-and-ticket", "The type is not valid.", i, 2));
            }
            if( Validate.checkType(csv.get(i)[2]) && !Validate.isValidStatus(csv.get(i)[2], csv.get(i)[3])){
                errors.add(new Error("Lead-and-ticket", "The status is not valid for this type.", i, 3));
            }
            System.out.print(Formatter.formatToDouble(csv.get(i)[4])+"\n");
            if(!Validate.checkAmount(Formatter.formatToDouble(csv.get(i)[4]))){
                errors.add(new Error("Lead-and-ticket", "Amount value cannot be negative.", i, 4));
            }
        }
        return errors;
    }
    
    private List<String[]> filterByType(List<String[]> data, String type) {
        return data.stream()
                .filter(row -> row != null && row.length > 2 && type.equalsIgnoreCase(row[2]))
                .collect(Collectors.toList());
    }

    public void saveTicketAndLead(List<String[]> csv, List<Customer> customers, User connected ){
        List<String[]> tickets = filterByType(csv, "ticket");
        List<String[]> leads = filterByType(csv, "lead");

        List<Ticket> savedTickets = ticketService.instanceAll(tickets, customers, connected);
        List<Lead> savedLeads = leadService.instanceAll(leads, customers, connected);
        ticketService.saveAll(savedTickets);
        leadService.saveAll(savedLeads);
    }

    public List<Error> importData(MultipartFile customerFile, MultipartFile ticketAndLead, User connected)throws Exception{
        try {
            
            List<String[]> csv = new ImportTemplate().readCsvFile(customerFile);
            List<String[]> leadtickcsv = new ImportTemplate().readCsvFile(ticketAndLead);
            List<Error> errors = customerService.checkCustomerError(csv);
            errors.addAll(checkErrorTicketLead(leadtickcsv, csv));
    
            if(errors.isEmpty()){
                List<Customer> customers2 = customerService.saveCustomerWProfile(csv, connected);
                saveTicketAndLead(leadtickcsv, customers2, connected);
            }
            else {
                for (Error error : errors) {
                    System.out.println(error.getMessage()+" row: "+error.getRowNum()+" file "+error.getFile() );
                }
            }
            return errors;
        } catch (Exception e) {
            throw e;
        }
    }
}
