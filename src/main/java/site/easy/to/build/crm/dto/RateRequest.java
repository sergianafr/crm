package site.easy.to.build.crm.dto;

public class RateRequest {
    private double rate;
    private int idUser;
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }public int getIdUser() {
        return idUser;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
