package site.easy.to.build.crm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.auto.value.AutoAnnotation;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.ImportService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.utility.Error;
import site.easy.to.build.crm.utility.ImportTemplate;


@Controller
@RequestMapping("/employee/import")
public class ImportController {
    @Autowired private CustomerService customerService;
    @Autowired private AuthenticationUtils authenticationUtils;
    @Autowired private UserService userService;
    @Autowired private ImportService importService;
    @PostMapping()
    public String importData(@RequestParam("customers") MultipartFile customers,@RequestParam("leadticket") MultipartFile leadticket, @RequestParam("budget") MultipartFile budget,Model model, org.springframework.security.core.Authentication authentication) {
        // List<String> errors = new ArrayList<>();
        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            User conn = userService.findById(userId);
            List<Error> errors = importService.importData(customers, leadticket, budget, conn);
            model.addAttribute("importErrors", errors); // Ajoutez cette ligne
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("globalError", "Une erreur inattendue s'est produite lors de l'importation.");
        }
        return "import-page";
    }

    @GetMapping("/import-page")
    public String getMethodName(Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        return "import-page";
    }
    
}
