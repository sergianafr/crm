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
            c.connectToMySQL();
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
    // private void insertToTemp(List<String[]> data, String[] dataType, Connect c)throws Exception{
    //     String insertSQL = "INSERT INTO temp_espace (nom, prix_heure) VALUES (?, ?);";
    //     try {
    //         try (PreparedStatement pstmt = c.getConnex().prepareStatement(insertSQL)) {
    //             for (int i = 1; i < data.size(); i++) { // Commencer à 1 pour ignorer l'en-tête
    //                 String[] row = data.get(i);
    //                 pstmt.setString(1, row[0]);  // Colonne "name"
    //                 pstmt.setDouble(2, Double.parseDouble(row[1])); // Colonne "age"
    //                 pstmt.addBatch();
    //             }
    //             pstmt.executeBatch();
    //         }
    //     } catch (Exception e) {
    //         throw e;
    //     }
    // }
    public static List<Object> validateData(String[] data, String[] dataType, int[] notNull, int rowCount,List<String> errors)throws Exception{
        List<Object> transformed = new ArrayList<>();
        int count = -1;
        // int rowcount = 0;
        for (int i = 0; i < data.length; i++) {
            if(notNull[count++] == i && (data[i].isEmpty() || data[i] == null)){
                errors.add("Colonne "+i+1+" est nulle ou vide a la ligne "+rowCount);
            }else {
                transformed.add(Formatter.transform(data[i], dataType[i], rowCount, i, errors));
            }

        }
        return transformed;
    }
    public static void insertToTemp(List<String[]> data, String[] dataTypes, int[] notNull,String tableName, String columns, Connect c)throws Exception{
        String insertSQL = getQueryInsert(tableName, columns, dataTypes.length);
        List<String> errors = new ArrayList<>();
        try {
            try (PreparedStatement pstmt = c.getConnex().prepareStatement(insertSQL)) {
                for (int i = 1; i < data.size(); i++) { // Commencer à 1 pour ignorer l'en-tête
                    String[] row = data.get(i);
                    List<Object> transformed = validateData(row, dataTypes, notNull, i, errors);
                    if(errors.size() < 0){
                        for (int j = 0; j < transformed.size(); j++) {
                            pstmt.setObject(j+1, transformed.get(j));
                        }
                        pstmt.addBatch();
                    } else {
                        for (String error : errors) {
                            System.out.println(error);
                        }
                    }
                }
                pstmt.executeBatch();
            }
        } catch (Exception e) {
            throw e;
        }
    }
    private static String getQueryInsert(String tableName, String columns, int size){
        String query = "INSERT INTO "+tableName+"("+columns+") VALUES (";
        for (int i = 0; i < size; i++) {
            query+="?,";
        }
        return query.substring(0, query.length()-1)+")";
    }
}
