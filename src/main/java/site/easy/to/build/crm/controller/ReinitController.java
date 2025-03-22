package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.easy.to.build.crm.service.reinit.ReinitService;

@Controller
public class ReinitController {
    @Autowired
    private ReinitService reinitService;

    @GetMapping("/reinit")
    public String reinitDatabase() {
        reinitService.resetDatabase();
        return "import-page"; 
    }
}
