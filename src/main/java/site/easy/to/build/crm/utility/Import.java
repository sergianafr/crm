package site.easy.to.build.crm.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Import {
    public static List<String[]> readCsvFile(MultipartFile file) throws Exception{
        List<String[]> records = new ArrayList<String[]>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            records = csvReader.readAll(); // Lire tout le fichier
            if (!records.isEmpty()) {
                String[] header = records.get(0); // Récupérer l'en-tête
                System.out.println("Header: " + String.join(", ", header));

                // Lire les données ligne par ligne
                for (int i = 1; i < records.size(); i++) {
                    String[] row = records.get(i);
                    System.out.println("Row " + i + ": " + String.join(", ", row));
                }
            }

        } catch (IOException | CsvException e) {
           throw e;
        }
        return records;
    }
    public static List<String[]> readCsvFile(String fileName) throws Exception {
        List<String[]> records = new ArrayList<>();

        try (InputStream inputStream = new ClassPathResource(fileName).getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            CSVReader csvReader = new CSVReader(streamReader)) {

            records = csvReader.readAll();
            if (!records.isEmpty()) {
                System.out.println("Header: " + String.join(", ", records.get(0)));
                for (int i = 1; i < records.size(); i++) {
                    System.out.println("Row " + i + ": " + String.join(", ", records.get(i)));
                }
            }

        } catch (IOException | CsvException e) {
            throw e;
        }

        return records;
    }
    public static String queryCreateTemp(String[] header, String[] types, String name){
        String query = "CREATE TEMP TABLE "+name+"("+header[0]+" "+types[0];
        for (int i = 1; i < types.length; i++) {
            query+=","+header[i]+" "+types[i];
        }
        return query + ")";
    }
    public static void createTemp(String query, Connect c)throws Exception{
        System.out.println(query);
        // Connect c = new Connect();
        try {
            c.connectToPostgres();
            PreparedStatement ps = c.getConnex().prepareStatement(query);
            ps.executeUpdate();
            ps.close();
            // c.getConnex().commit();
        } catch (Exception e) {
            throw e;
        }
    }
    public static void callFunction(String funcName, Connect c)throws Exception{
        try {
            String query = "SELECT "+funcName;
        
            PreparedStatement ps = c.getConnex().prepareStatement(query);
            ps.executeQuery();
            ps.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
