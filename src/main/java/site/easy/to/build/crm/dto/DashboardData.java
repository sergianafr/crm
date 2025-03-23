package site.easy.to.build.crm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import site.easy.to.build.crm.entity.*;

public class DashboardData {

    private int nbCustomers;
    private int nbTickets;
    private int nbLeads;
    private BigDecimal totalBudget;

    // Constructeurs, getters et setters
    public DashboardData( int nbCustomers, int nbTickets, int nbLeads) {

        this.nbCustomers = nbCustomers;
        this.nbTickets = nbTickets;
        this.nbLeads = nbLeads; 

    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }
    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public int getNbCustomers() {
        return nbCustomers;
    }


    public void setNbCustomers(int nbCustomers) {
        this.nbCustomers = nbCustomers;
    }


    public int getNbTickets() {
        return nbTickets;
    }


    public void setNbTickets(int nbTickets) {
        this.nbTickets = nbTickets;
    }


    public int getNbLeads() {
        return nbLeads;
    }


    public void setNbLeads(int nbLeads) {
        this.nbLeads = nbLeads;
    }


}