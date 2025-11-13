package project.gasnow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/jido")
    public String jido() {
        return "/pages/jido.html";
    }

    @GetMapping("/favorites")
    public String favorites() { return "/pages/favorites.html";
    }

    @GetMapping("/login")
    public String pageLogin() {return "pages/login";}

    @GetMapping("/register")
    public String pageRegister() {return "pages/register";}

    @GetMapping("/chart")
    public String pageChart() {return "pages/chart.html";}

    @GetMapping("/mypage")
    public String pageMypage() {return "pages/mypage.html";}
}
