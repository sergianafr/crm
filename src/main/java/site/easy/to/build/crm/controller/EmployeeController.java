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

import site.easy.to.build.crm.service.EmployeeService;
import site.easy.to.build.crm.util.AuthenticationUtils;


@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping("/import-page")
    public String getMethodName(Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        return "import-page";
    }
    @PostMapping("/import")
    public String importEmployees(@RequestParam("file") MultipartFile file, Model model) {
        List<String> errors = new ArrayList<>();
        try {
            errors = employeeService.importEmp(file);
            if(errors != null){
                model.addAttribute("errors", errors);
            } else {
                System.out.println("Importation réussie");
                model.addAttribute("success", "Importation réussie");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(e.getMessage());
            model.addAttribute("errors", errors);
        } 
        return "import-page";
    }
    //  @PostMapping("/import")
    // public String importEmployees(@RequestParam("file") MultipartFile file, Model model) {
    //     try {
    //         employeeService.importEmployee(file);
    //         System.out.println("Importation réussie");
    //         model.addAttribute("message", "Importation réussie");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         model.addAttribute("message", "Erreur lors de l'importation");
    //     } 
    //     return "import-page";
    // }
}
