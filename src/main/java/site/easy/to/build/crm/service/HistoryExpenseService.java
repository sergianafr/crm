package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.HistoryExpense;
import site.easy.to.build.crm.repository.HistoryExpenseRepository;

@Service
public class HistoryExpenseService {
    @Autowired
    private HistoryExpenseRepository historyExpenseRepository;

    public HistoryExpense save(HistoryExpense historyExpense) {
        return historyExpenseRepository.save(historyExpense);
    }
    public HistoryExpense saveHistoryExpense(Expense expense) {
        HistoryExpense historyExpense = new HistoryExpense(expense);
        return historyExpenseRepository.save(historyExpense);
    }
}
