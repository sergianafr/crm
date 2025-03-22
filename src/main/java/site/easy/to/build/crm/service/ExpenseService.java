package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;

@Service
public class ExpenseService {
   @Autowired private ExpenseRepository expenseRepository;
   @Autowired private HistoryExpenseService historyExpenseService;
   
   public Expense save(Expense expense) {
        expenseRepository.save(expense);
        historyExpenseService.saveHistoryExpense(expense);
        return expense;
   }
}
