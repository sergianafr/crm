package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.easy.to.build.crm.entity.HistoryExpense;

public interface HistoryExpenseRepository extends JpaRepository<HistoryExpense, Long> {
    public HistoryExpense save(HistoryExpense historyExpense);
    
}
