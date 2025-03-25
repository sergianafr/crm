package site.easy.to.build.crm.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class ImportTemplate {
    @Autowired private JdbcTemplate jdbcTemplate;
     public List<String[]> readCsvFile(MultipartFile file) throws Exception {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            boolean isHeader = true;
            while ((line = csvReader.readNext()) != null) {
                if (isHeader) {
                    System.out.println("Header: " + String.join(", ", line));
                    isHeader = false;
                } else {
                    System.out.println("Row: " + String.join(", ", line));
                }
                records.add(line);
            }
        } catch (IOException | CsvException e) {
            throw e;
        }
        return records;
    }

    // Lecture d'un fichier CSV depuis les ressources du classpath
    public List<String[]> readCsvFile(String fileName) throws Exception {
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

    // Création d'une table temporaire
    public void createTempTable(String[] header, String[] types, String name) {
        String query = "CREATE TEMP TABLE " + name + " (" + header[0] + " " + types[0];
        for (int i = 1; i < types.length; i++) {
            query += "," + header[i] + " " + types[i];
        }
        query += ")";
        System.out.println(query);
        jdbcTemplate.execute(query);
    }

    // Appel d'une fonction stockée
    public void callFunction(String funcName) {
        String query = "SELECT " + funcName + "()"; // Ajout de () pour appeler la fonction
        jdbcTemplate.execute(query);
    }

    // Validation des données
    public List<Object> validateData(String[] data, String[] dataType, int[] notNull, int rowCount, List<String> errors) {
        List<Object> transformed = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            final int currentIndex = i;  // Create a final copy of the loop variable
            boolean isNull = data[i] == null || data[i].trim().isEmpty() || data[i].equalsIgnoreCase("null");
    
            if (Arrays.stream(notNull).anyMatch(n -> n == currentIndex) && isNull) {
                errors.add("Colonne " + (i + 1) + " est nulle ou vide à la ligne " + rowCount);
            } else {
                transformed.add(isNull ? null : Formatter.transform(data[i], dataType[i], rowCount, i, errors));
            }
        }
        return transformed;
    }
    // Vérification des erreurs dans les données
    public List<String> getErrors(List<String[]> data, String[] dataTypes, int[] notNull) {
        List<String> errors = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            validateData(data.get(i), dataTypes, notNull, i, errors);
        }
        return errors;
    }

    // Insertion des données dans la table temporaire
    public void insertToTemp(List<String[]> data, String[] dataTypes, int[] notNull, String tableName, String columns) throws Exception {
        String insertSQL = getQueryInsert(tableName, columns, dataTypes.length);
        List<String> errors = getErrors(data, dataTypes, notNull);

        if (errors.isEmpty()) {
            List<Object[]> batchArgs = new ArrayList<>();
            for (int i = 1; i < data.size(); i++) { // Ignorer l'en-tête
                batchArgs.add(validateData(data.get(i), dataTypes, notNull, i, errors).toArray());
            }
            jdbcTemplate.batchUpdate(insertSQL, batchArgs);
        } else {
            throw new Exception("Erreur dans les données : " + String.join(", ", errors));
        }
    }

    // Construction de la requête d'insertion
    private String getQueryInsert(String tableName, String columns, int size) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (" + columns + ") VALUES (");
        query.append("?,".repeat(size));
        return query.substring(0, query.length() - 1) + ")";
    }

    public boolean controlUnique(String tableName, String column, Object value) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + column + " = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, value);
        return count != null && count == 0;
    }
}

