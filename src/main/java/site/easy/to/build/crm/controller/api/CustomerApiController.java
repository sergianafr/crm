package site.easy.to.build.crm.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Rate;
import site.easy.to.build.crm.service.RateService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> calculateExpense(@RequestParam Integer customerId,
                                                            @RequestParam BigDecimal amount) {
        try {
            BigDecimal actualExpense = customerService.getTotalExpense(customerId);
            BigDecimal totalBudget = customerService.getTotalBudget(customerId);
            Rate maxRate = rateService.findMax();
            BigDecimal rateValue = totalBudget.multiply(BigDecimal.valueOf(maxRate.getRate()).divide(BigDecimal.valueOf(100)));

            BigDecimal totalExpenseWithAmount = actualExpense.add(amount);

            Map<String, Object> response = new HashMap<>();
            response.put("maxRate", maxRate.getRate());
            response.put("maxValueRate", rateValue);
            response.put("totalBudget", totalBudget);

            if (totalExpenseWithAmount.compareTo(rateValue) <= 0) {
                response.put("status", "valid");
            } else if (totalExpenseWithAmount.compareTo(rateValue) >= 0 && totalExpenseWithAmount.compareTo(totalBudget) <= 0) {
                response.put("status", "superior");
            } else if (totalExpenseWithAmount.compareTo(totalBudget) >= 0) {
                response.put("status", "exceed");
            } else {
                response.put("status", "valid");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}
