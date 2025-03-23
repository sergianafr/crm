package site.easy.to.build.crm.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Rate;
import site.easy.to.build.crm.service.RateService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/customer")
public class CustomerApiController {
    @Autowired private CustomerService customerService;
    @Autowired private RateService rateService;
    @GetMapping("/checkExpense")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<String> calculateExpense(@RequestParam Integer customerId,
    @RequestParam BigDecimal amount) {
        try {
            // Integer customerId = Integer.parseInt(request.get("customerId").toString());
            // BigDecimal amount = new BigDecimal(request.get("amount").toString());
            BigDecimal actualExpense = customerService.getTotalExpense(customerId);
            System.out.println(actualExpense);
            BigDecimal totalBudget = customerService.getTotalBudget(customerId);
            System.out.println(totalBudget);
            Rate maxRate = rateService.findMax();
            BigDecimal rateValue = totalBudget.multiply(BigDecimal.valueOf(maxRate.getRate()).divide(BigDecimal.valueOf(100)));
            System.out.println(rateValue);
            
            BigDecimal totalExpenseWithAmount = actualExpense.add(amount);

            if (totalExpenseWithAmount.compareTo(rateValue) <= 0) {
                return ResponseEntity.ok("valid");
            } else if (totalExpenseWithAmount.compareTo(rateValue) >= 0 && totalExpenseWithAmount.compareTo(totalBudget) <= 0) {
                return ResponseEntity.ok("superior");
            } else if(totalExpenseWithAmount.compareTo(totalBudget) >= 0) {
                return ResponseEntity.ok("exceed");
            }
            return ResponseEntity.ok("valid");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
}
