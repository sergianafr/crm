package site.easy.to.build.crm.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_expense")
public class HistoryExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descriptions;

    @Column(name = "date_update", nullable = false, updatable = false)
    private LocalDateTime dateUpdate;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "id_expense", nullable = false)
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    // Constructeurs, getters et setters

    public HistoryExpense() {
        // Constructeur par défaut requis par JPA
    }

    public HistoryExpense(String descriptions, BigDecimal amount, Expense expense, User user) {
        this.descriptions = descriptions;
        this.amount = amount;
        this.expense = expense;
        this.user = user;
        this.dateUpdate = LocalDateTime.now(); // Définit la date de mise à jour automatiquement
    }

    public HistoryExpense(Expense expense){
        this.expense = expense;
        this.descriptions = expense.getDescriptions();
        this.amount = expense.getAmount();
        this.user = expense.getUser();
        this.dateUpdate = LocalDateTime.now(); // Définit la date de mise à jour automatiquement
    }
    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "HistoryExpense{" +
                "id=" + id +
                ", descriptions='" + descriptions + '\'' +
                ", dateUpdate=" + dateUpdate +
                ", amount=" + amount +
                ", expense=" + expense +
                ", user=" + user +
                '}';
    }
}
