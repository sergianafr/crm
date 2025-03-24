package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.dto.UserWRole;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/login")
public class LoginApi {
    @Autowired UserService userService;
    
    @PostMapping("/")
    public ResponseEntity<?> checkUser(@RequestBody String email) {
        System.out.println(email);
        User user = userService.findByEmail(email);        
        if (user == null) {
            System.out.println(email);
            return ResponseEntity.notFound().build();
        }

        if (userService.isUserManager(user)) {
            return ResponseEntity.ok(UserWRole.fromEntity(user));
        } else {
            return ResponseEntity.status(403).body("Accès refusé : rôle insuffisant");
        }
    }
    
    
}
