package site.easy.to.build.crm.utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    private static final String DATABASE = "crm";
    private static final String PASSWORD = "root";
    private Connection connex;

    public Connection getConnex() {
        return connex;
    }
    public void setConnex(Connection connex) {
        this.connex = connex;
    }
    public Connect (){
    }
    public void closeBD()
    {
        try 
        {
            if(this.connex != null){
                this.connex.close();
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void rollback()
    {
        try
        {
            this.connex.rollback();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void connectToPostgres() throws Exception{
        try{
            Class.forName("org.postgresql.Driver");
            this.connex = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+DATABASE+"?charSet=UTF-8", "postgres", PASSWORD);
            this.connex.setAutoCommit(false);
        }
        catch(Exception e){
            throw e;
        }
    }
    public void connectToMySQL() throws Exception {
        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");    
            this.connex = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + DATABASE + "?useUnicode=true&characterEncoding=UTF-8", 
                "root", 
                PASSWORD
            );
                this.connex.setAutoCommit(false);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
