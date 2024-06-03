package Capstone.Davinchi_Server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RunController {
    @GetMapping("/run")
    public String runCheck(){
        return "cloud run!";
    }
}
