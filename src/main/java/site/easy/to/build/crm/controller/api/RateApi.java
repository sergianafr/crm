package site.easy.to.build.crm.controller.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.service.RateService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import site.easy.to.build.crm.dto.RateRequest;
import site.easy.to.build.crm.entity.Rate;
import site.easy.to.build.crm.entity.User;
@RestController
@RequestMapping("/api/rate")
public class RateApi {
    @Autowired private RateService rateService;
    @Autowired private AuthenticationUtils authenticationUtils;
    @Autowired private UserService userService;

    @PostMapping("/save")
    @CrossOrigin(origins = "http://localhost:5174")
    public String saveRate(@RequestBody RateRequest rateRequest) {
        System.out.println(rateRequest.getRate());
        try {
            // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // User connected = userService.findById(authenticationUtils.getLoggedInUserId(authentication));
            User connected = userService.findById(rateRequest.getIdUser());

            System.out.println(connected.getId());
            Rate rate = new Rate();
            rate.setRate(rateRequest.getRate());
            rate.setUser(connected);
            rate.setCreatedAt(LocalDateTime.now());
            System.out.println(rate.getRate());
            rateService.save(rate);
            return "{\"success\": true}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false}";
        }
         // Remplacez par votre logique de sauvegarde
    }
    


}
