package org.springframework.samples.petclinic.logins;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginsController {
    private final LoginsRepository repository;
    
    public LoginsController(LoginsRepository repository){
        this.repository = repository;   
    }
    
    @GetMapping("/logins")
    public String getLogins(Map<String, Object> model) {      
        List<LoginsEntity> logins = this.repository.findAll();
        model.put("selections", logins);
        return "users/loginsReport";
    }
}
