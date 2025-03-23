package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    public Expense save(Expense expense);
    public List<Expense> findByLead(Lead lead);
    public List<Expense> findByTicket(Ticket ticket);
    
}
