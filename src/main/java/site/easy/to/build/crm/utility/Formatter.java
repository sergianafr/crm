package site.easy.to.build.crm.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class Formatter {
    private static final List<String> DATE_FORMATS = Arrays.asList(
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "dd-MM-yyyy", 
        "dd/MM/yyyy", 
        "MM/dd/yyyy", 
        "yyyy.MM.dd",
        "MM-dd-yyyy"
    );
    private static final List<String> TIMESTAMP_FORMATS = Arrays.asList(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "dd-MM-yyyy HH:mm:ss", 
        "dd/MM/yyyy HH:mm:ss",
        "MM/dd/yyyy HH:mm:ss", 
        "yyyy.MM.dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm"
    );

    // Formats d'heure possibles
    private static final List<String> TIME_FORMATS = Arrays.asList(
        "HH:mm:ss", // Format standard
        "HH:mm",    // Format sans secondes
        "HH.mm.ss", // Format avec points
        "HH,mm,ss"  // Format avec virgules
    );


    /**
     * Transforme une date de n'importe quel format en format MySQL "yyyy-MM-dd".
     *
     * @param date La date à transformer.
     * @return La date au format "yyyy-MM-dd", ou null si aucun format ne correspond.
     */
    public static String transformDate(String date,int row, int col,List<String> errors) {
        // Définir le format de sortie (celui de MySQL)
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Essayer chaque format de date jusqu'à ce qu'une correspondance soit trouvée
        for (String format : DATE_FORMATS) {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(format);

            try {
                // Parser la date d'entrée
                LocalDate parsedDate = LocalDate.parse(date, inputFormat);
                // Formater la date en sortie
                return parsedDate.format(outputFormat);
            } catch (DateTimeParseException e) {
                // Ignorer et essayer le prochain format
            }
        }

        // Si aucun format ne correspond
        errors.add("Aucun format de date ne correspond : " + date+ " ligne : "+row+" colonne : "+col);
        return null;
    }
    public static Object transform(String data, String type, int row, int col, List<String> errors) {
        type = type.toLowerCase();
        switch (type) {
            case "date":
                return transformDate(data,row, col, errors);
            case "string":
                return data;
            case "int":
                try {
                    return Integer.parseInt(data);
                } catch (NumberFormatException e) {
                    errors.add("La valeur '" + data + "' n'est pas un entier. ligne : "+row+" colonne : "+col);
                    return null;
                }
            case "float":
                try {
                    return Float.parseFloat(data);
                } catch (NumberFormatException e) {
                    errors.add("La valeur '" + data + "' n'est pas un nombre décimal. ligne : "+row+" colonne : "+col);
                    return null;
                }
            case "timestamp":
                return transformTimestamp(data, row,  col, errors);
            case "time":
                return transformTime(data, row, col, errors);
            default:
                errors.add("Type de données inconnu : " + type+" ligne : "+row+" colonne : "+col);
                return null;
        }
    }
    public static String transformTimestamp(String timestamp, int row, int col,List<String> errors) {
        // Définir le format de sortie (celui de MySQL)
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Essayer chaque format de timestamp jusqu'à ce qu'une correspondance soit trouvée
        for (String format : TIMESTAMP_FORMATS) {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(format);

            try {
                // Parser le timestamp d'entrée
                LocalDateTime parsedTimestamp = LocalDateTime.parse(timestamp, inputFormat);
                // Formater le timestamp en sortie
                return parsedTimestamp.format(outputFormat);
            } catch (DateTimeParseException e) {
                // Ignorer et essayer le prochain format
            }
        }

        // Si aucun format ne correspond
        errors.add("Aucun format de timestamp ne correspond : " + timestamp+ " ligne : "+row+" colonne : "+col);
        return null;
    }

    /**
     * Transforme une heure de n'importe quel format en format MySQL "HH:mm:ss".
     *
     * @param time   L'heure à transformer.
     * @param errors Une liste pour stocker les erreurs rencontrées.
     * @return L'heure au format "HH:mm:ss", ou null si aucun format ne correspond.
     */
    public static String transformTime(String time,int row, int col, List<String> errors) {
        // Définir le format de sortie (celui de MySQL)
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Essayer chaque format d'heure jusqu'à ce qu'une correspondance soit trouvée
        for (String format : TIME_FORMATS) {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(format);

            try {
                // Parser l'heure d'entrée
                LocalTime parsedTime = LocalTime.parse(time, inputFormat);
                // Formater l'heure en sortie
                return parsedTime.format(outputFormat);
            } catch (DateTimeParseException e) {
                // Ignorer et essayer le prochain format
            }
        }

        // Si aucun format ne correspond
        errors.add("Aucun format d'heure ne correspond : " + time+ " ligne : "+row+" colonne : "+col);
        return null;
    }

}
