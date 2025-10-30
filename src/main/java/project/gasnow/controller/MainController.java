package project.gasnow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/aaaa")
    public String pageMain() {return "index";}
}
