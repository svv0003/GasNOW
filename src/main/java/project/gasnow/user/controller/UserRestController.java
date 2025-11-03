package project.gasnow.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.gasnow.common.util.SessionUtil;
import project.gasnow.user.model.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserRestController {

    private final UserService userService;

    // 회원가입

    // 로그인
    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam String userId,
                        @RequestParam String password,
                        Model model){
        return userService.login(session, userId, password);
    };
}
