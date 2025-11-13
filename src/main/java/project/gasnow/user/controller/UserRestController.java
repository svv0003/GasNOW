package project.gasnow.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.gasnow.common.util.SessionUtil;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.service.UserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

    private final UserService userService;

    /**
     * 아이디 중복 체크 메서드
     * @param userId View에서 클라이언트가 작성한 유저 아이디
     * @return 아이디 중복 체크 성공 여부
     */
    @GetMapping("/api/check-userid")
    public boolean CheckUserId(@RequestParam String userId) {
        return userService.checkUserIdDuplicate(userId);
    }

    /**
     * 연락처 중복 체크 메서드
     * @param userPhone View에서 클라이언트가 작성한 연락처
     * @return 연락처 중복 체크 성공 여부
     */
    @GetMapping("/api/check-phone")
    public boolean CheckPhone(@RequestParam String userPhone) {
        return userService.checkPhoneDuplicate(userPhone);
    }

    /**
     * 이메일 전송 메서드
     * @param body
     * @return 이메일 전송 성공 시 1, 실패 시 0
     */
    @PostMapping("/api/send-email-code")
    public int sendEmailCode(@RequestBody Map<String, String> body, HttpSession session) {
        String userEmail = body.get("userEmail");
        String authKey = userService.sendEmail("email", userEmail);

        if(authKey == null) return 0;

        // 세션에 인증번호 저장
        session.setAttribute("authKey:" + userEmail, authKey);
        session.setAttribute("authTime:" + userEmail, System.currentTimeMillis());  // 이메일 전송 시각 저장

        return 1;
    }

    /**
     * 인증번호 비교(확인) 메서드
     * @param body
     * @param session
     * @return service 메서드에 map을 파라미터로 넘겨서 반환 <br>
     *         이메일이 존재하고, 인증번호가 일치하면 1 반환 <br>
     *         둘 중 하나라도 해당하지 않으면 0 반환
     */
    @PostMapping("/api/verify-email-code")
    public int checkAuthKey(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("userEmail");
        String inputCode = body.get("inputCode");  // 클라이언트가 보낸 인증키

        String savedCode = (String) session.getAttribute("authKey:" + email);  // 세션에 저장된 인증키
        Long authTime = (Long) session.getAttribute("authTime:" + email);      // 세션에 저장된 전송 시각

        log.info("세션에 저장된 인증키: {}", savedCode);

        // 유효시간 5분
        if (savedCode != null && authTime != null && (System.currentTimeMillis() - authTime) <= 5 * 60000) {
            if(savedCode.equals(inputCode)) {
                session.removeAttribute("authKey:" + email);
                session.removeAttribute("authTime:" + email);
                return 1;  // 검증 성공
            }
        }

        return 0;  // 검증 실패
    }

    /**
     * 회원가입 메서드
     * @param user js에서 body에 담아서 보낸 User 객체
     */
    @PostMapping("/api/register")
    public int register(@RequestBody User user) {
        return userService.register(user);
    };

    /**
     * 로그인 메서드
     * @param user
     * @param saveId
     * @param session
     * @param res
     * @param model
     * @return JSON 반환
     */
    // /api/login?saveId=on
    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user,
                        @RequestParam(required = false) String saveId,
                        HttpSession session,
                        HttpServletResponse res){

        log.info("req userId={}, userPwLen={}", user.getUserId(),
                user.getUserPassword() == null ? null : user.getUserPassword().length());


        String userId = user.getUserId();
        String userPassword = user.getUserPassword();

        user = userService.login(session, userId, userPassword);

        // 로그인 입력란에 아무것도 작성하지 않은 경우
        if(user == null) {
            // HTTP 상태 코드 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "ok", "false",
                    "error", "아이디 또는 비밀번호가 일치하지 않습니다."
            ));
        }

        SessionUtil.setLoginUser(session, user);  // 세션에 저장
        log.info("[LOGIN] set sessionId={}, hasLoginUser={}",
                session.getId(), SessionUtil.isLoginUser(session));

        // 아이디 저장 체크박스 체크 시 쿠키에 회원 정보 저장
        Cookie userIdCookie = new Cookie("saveId", userId);
        userIdCookie.setPath("/");
        if("on".equals(saveId)) {
            userIdCookie.setMaxAge(60 * 60 * 24 * 30);  // 30일
        } else {
            userIdCookie.setMaxAge(0);
        }

        res.addCookie(userIdCookie);

        return ResponseEntity.ok(Map.of(
                "ok", "true",
                "message", "로그인 성공",
                "redirect", "/"
        ));
    };
}
