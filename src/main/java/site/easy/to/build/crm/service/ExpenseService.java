package site.easy.to.build.crm.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.dto.ExpenseRequest;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

@Service
public class ExpenseService {
   @Autowired private UserService userService;
   @Autowired private ExpenseRepository expenseRepository;
   @Autowired private HistoryExpenseService historyExpenseService;
   @Autowired private LeadService leadService;  
   @Autowired private TicketService ticketService;
   public Expense save(Expense expense) {
        expenseRepository.save(expense);
        historyExpenseService.saveHistoryExpense(expense);
        return expense;
   }

   public Expense updateLead(ExpenseRequest request,int leadId){
      User connected = userService.findById(request.getIdUser());
      BigDecimal amount = request.getAmount();
      System.out.println(leadId+"leadIIIID");
      Lead l = leadService.findByLeadId(leadId);
      List<Expense> exp = expenseRepository.findByLead(l);
      Expense expe = new Expense();
      expe.setUser(connected);
      if(exp.isEmpty() || exp == null || exp.size()>1){
         expe = new Expense(l.getName(), amount, connected, null, l);
      }else {
         expe = exp.get(0);
         expe.setAmount(amount);
      }
      return save(expe);
   }
   public Expense updateTicket(ExpenseRequest request, int ticketId){
      User connected = userService.findById(request.getIdUser());
      System.out.println(ticketId+"tikeee");
      BigDecimal amount = request.getAmount();
      Ticket l = ticketService.findByTicketId(ticketId);
      System.out.println(l.getDescription());
      List<Expense> exp = expenseRepository.findByTicket(l);
      Expense expe = new Expense();
      expe.setUser(connected);
      if(exp.isEmpty() || exp == null || exp.size()>1){
         expe = new Expense(l.getDescription(), amount, connected, l, null);
      }else {
         expe = exp.get(0);
         expe.setAmount(amount);
      }
      return save(expe);
   }

   public BigDecimal getTotalLead(){
      List<Lead> leads = leadService.findAll();
      BigDecimal total = BigDecimal.ZERO;
      for (Lead lead : leads) {
         total = total.add(leadService.getTotalExpense(lead));
      }
      
      return total;
   }
   public BigDecimal getTotalTicket(){
      List<Ticket> leads = ticketService.findAll();
      BigDecimal total = BigDecimal.ZERO;
      for (Ticket lead : leads) {
         total = total.add(ticketService.getTotalExpense(lead));
      }
      return total;
   }

   
}
