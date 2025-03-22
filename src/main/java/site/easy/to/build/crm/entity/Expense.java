package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descriptions;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_ticket")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_lead")
    private Lead lead;

    // Constructeurs, getters et setters

    public Expense() {
        // Constructeur par défaut requis par JPA
    }

    public Expense(String descriptions, BigDecimal amount, User user, Ticket ticket, Lead lead) {
        this.descriptions = descriptions;
        this.amount = amount;
        this.user = user;
        this.ticket = ticket;
        this.lead = lead;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", descriptions='" + descriptions + '\'' +
                ", amount=" + amount +
                ", user=" + user +
                ", ticket=" + ticket +
                ", lead=" + lead +
                '}';
    }
}