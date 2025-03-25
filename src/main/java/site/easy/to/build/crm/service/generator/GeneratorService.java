package site.easy.to.build.crm.service.generator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
@Service
public class GeneratorService {
    private final static Faker faker = new Faker(new Locale("fr"));
    public String generateLoremIpsum(){
        String loremIpsum = faker.lorem().paragraph(1);
        return loremIpsum;
    }
    public String generatePhone(){
        return faker.phoneNumber().phoneNumber();
    }
    public HashMap<String, String> generateLocation(){
        HashMap<String, String> location = new HashMap<>();

        location.put("address", faker.address().streetAddress());
        location.put("city", faker.address().city());
        location.put("state", faker.address().state()); // Ou region pour certains pays
        location.put("country", "France");

        return location;
    }
    public String formatUsername(String string){
        String[] ifEmail = string.split("@");
        String[] espace = string.split(" ");
        if(ifEmail.length > 1){
            return ifEmail[0];
        } else if(espace.length > 1){
            return espace[0]+espace[1];
        }
        return string;
    }
    public HashMap<String, String> generateLink(String username){
        HashMap<String, String> links = new HashMap<>();
        String format = formatUsername(username);
        links.put("youtube", "youtube/"+format);
        links.put("twitter", "twitter/"+format);
        links.put("facebook", "facebook/"+format);
        return links;
    }

    public LocalDateTime generateDate(LocalDateTime debut, LocalDateTime fin){
        return faker.date()
               .between(
                   java.sql.Timestamp.valueOf(debut),
                   java.sql.Timestamp.valueOf(fin)
               )
               .toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDateTime();
    }
    public  String generatePriority(){
        
        String[] myList = Arrays.asList("low", "medium", "high", "closed", "urgent", "critical").toArray(new String[0]);
        return faker.options().option(myList);
    }
}
