package site.easy.to.build.crm.controller.api;

import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.dto.export.CustomerExportDto;
import site.easy.to.build.crm.service.customer.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/import")
public class ImportApi {
    @Autowired CustomerService customerService;
    @PostMapping("/")
    public ResponseEntity<String> importFile(@RequestBody CustomerExportDto customer) {
        try {
            System.out.println(customer);
            // Validation des données reçues
            if (customer == null) {
                return ResponseEntity.badRequest().body("Données client manquantes");
            }
            
            // Appel du service
            customerService.createFromCopy(customer);
            
            // Réponse en cas de succès
            return ResponseEntity.ok("success");
            
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Gestion des erreurs métier
            return ResponseEntity.badRequest().body("Erreur de validation: " + e.getMessage());
            
        } catch (Exception e) {
            // Journalisation de l'erreur complète
            e.printStackTrace();
            
            // Réponse d'erreur avec le détail
            return ResponseEntity.internalServerError()
                .body("Erreur lors du traitement: " + e.getMessage());
        }
    }
    
}
