package site.easy.to.build.crm.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validate {
    private static final Set<String> TICKET_STATUS = Set.of(
        "open", "assigned", "on-hold", "in-progress", "resolved",
        "closed", "reopened", "pending-customer-response", "escalated", "archived"
    );
    private static final Set<String> LEAD_STATUS = Set.of(
        "meeting-to-schedule",
        "scheduled",
        "archived",
        "success",
        "assign-to-sales"
    );
    private static final HashMap<String, Set<String>> status = new HashMap<>();
    
    public static boolean checkEmail(String email){
        return email.contains("@") && email.contains(".");
    }
    public static boolean checkAmount(String amount){
        if(Double.valueOf(amount)<0){
            return false;
        }
        return true;
    }
    public static boolean checkType(String type){
        type.toLowerCase();
        if(type.equals("ticket") || type.equals("lead")){
            return true;
        } return false;
    }

    public static boolean isValidStatus(String type, String status) {
        if (type == null || status == null) {
            return false;
        }
        
        switch (type.toLowerCase()) {
            case "ticket":
                return TICKET_STATUS.contains(status.trim().toLowerCase());
            case "lead":
                return LEAD_STATUS.contains(status.trim().toLowerCase());
            default:
                return false; // ou lever une exception si le type est invalide
        }
    }
    public static boolean isValidStatus(String status) {
        return status != null && TICKET_STATUS.contains(status.trim().toLowerCase()) || status != null && LEAD_STATUS.contains(status.trim().toLowerCase());
    }

    public static boolean isDuplicate(List<String[]> list, int columnIndex, String valueToCheck) {
        int count = 0;
        for (String[] row : list) {
            if (columnIndex < row.length && row[columnIndex].equals(valueToCheck)) {
                count++;
                if (count > 1) {
                    return true; // Dès qu'on trouve un deuxième, on sait que c'est dupliqué
                }
            }
        }
        return false; // Aucun doublon trouvé
    } 
    public static List<Error> getDuplicates(List<String[]> list, int columnIndex, String fileName) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        List<Error> errors = new ArrayList<>();
        int rowNum = 0;
        for (int i = 1; i<list.size(); i++) {
            rowNum++;
            String[] row = list.get(i);
            if (columnIndex < row.length) {

                String value = row[columnIndex];
                if (!seen.add(value)) { // Si déjà présent, c'est un duplicata
                    String message = value+" value cannot be duplicated.";
                    duplicates.add(value);
                    errors.add(new Error(fileName, message, rowNum, columnIndex));
                }
            }
        }
        
        return errors;
    }

    public static boolean containsValue(List<String[]> list, int columnIndex, String searchValue) {
        // if (searchValue == null) {
        //     return containsInColumn(list, columnIndex, null);
        // }
        
        return list.stream()
            .filter(row -> row != null && columnIndex < row.length && row[columnIndex] != null)
            .anyMatch(row -> row[columnIndex].equalsIgnoreCase(searchValue));
    }
    //     // Convertir en minuscules et supprimer les espaces avant/après
    //     String normalizedType = type.toLowerCase().trim();
        
    //     // Vérifier les cas "ticket", "tickets", "lead", "leads"
    //     return normalizedType.equals("ticket") 
    //            || normalizedType.equals("tickets")
    //            || normalizedType.equals("lead")
    //            || normalizedType.equals("leads");
    // }
    // public boolean checkOne(String data, String type){
    //     switch (type) {
    //         case "email":
    //             return checkEmail(data);
    //         case ""
    //         default:
    //             break;
    //     }
    // }

    // public static List<Error> checkErrors(List<String[]> csv, String[] columnTypes){
    //     List<Error> errors = new ArrayList<>();
    //     for (int i = 1; i < csv.size(); i++) {
    //         for (int j = 0; j < csv.get(i).length; j++) {

    //         }
    //     }
    // } 

}
