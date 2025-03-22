package site.easy.to.build.crm.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/budget")
public class BudgetController {
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired 
    private BudgetService budgetService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @GetMapping("/create")
    public String showSaveForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        model.addAttribute("customers",customerService.findAll());
        model.addAttribute("budget", new Budget());
        return "budget/create-budget";
    }
    @PostMapping("/create")
    public String createBudget(@ModelAttribute("budget") Budget budget, BindingResult result, @RequestParam("customerId") Integer customerId,Authentication authentication,Model model) {
        model.addAttribute("customers", customerService.findAll());
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            // Si il y a des erreurs de validation, retourner à la vue du formulaire
            return "budget/create-budget";
        }
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        budget.setCreatedAt(LocalDateTime.now());
        budget.setUser(userService.findById(userId));
        budget.setCustomer(customerService.findByCustomerId(customerId));

        // Traiter l'objet budget (par exemple, le sauvegarder en base de données)
        budgetService.save(budget);
        model.addAttribute("success", "Budget créé avec succès !");
        System.out.println("Budget créé avec succès !");
        // Rediriger vers une autre page (par exemple, la liste des budgets)
        return "budget/create-budget";
    }
    
}
