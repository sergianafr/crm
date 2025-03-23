package site.easy.to.build.crm.utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FrontFormatter {
    public static String formatLocalDateTime(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
    public static String formatCurrency(BigDecimal currency){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setGroupingSeparator(','); 
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);  
        return formatter.format(currency);
    }
}
