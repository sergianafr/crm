package site.easy.to.build.crm.dto;

import java.time.LocalDateTime;
import java.util.List;

import site.easy.to.build.crm.entity.*;

public class DashboardData {

    private int nbCustomers;
    private int nbTickets;
    private int nbLeads;
    private LocalDateTime datei ;
    private String[] string = {"b", "b"};
    // Constructeurs, getters et setters
    public DashboardData( int nbCustomers, int nbTickets, int nbLeads) {

        this.nbCustomers = nbCustomers;
        this.nbTickets = nbTickets;
        this.nbLeads = nbLeads;
        this.string = new String[2];
        string[0]="&";
        string[1]="j";
        datei = LocalDateTime.now();
    }

    public LocalDateTime getDatei() {
        return datei;
    }
    public void setDatei(LocalDateTime datei) {
        this.datei = datei;
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
    public String[] getString() {
        return string;
    }public void setString(String[] string) {
        this.string = string;
    }


}