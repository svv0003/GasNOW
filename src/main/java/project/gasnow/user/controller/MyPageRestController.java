package project.gasnow.user.controller;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gasnow.common.util.SessionUtil;
import project.gasnow.user.model.dto.ReviewItemDTO;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;
import project.gasnow.user.model.service.MyPageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyPageRestController {
    private final MyPageService myPageService;

    /**
     * 세션에서 loginUser 데이터 가져오는 메서드
     * @param session
     * @return
     */
    private String getLoginUserId(HttpSession session) {
        log.info("[MYPAGE] get sessionId={}, hasLoginUser={}",
                session.getId(), SessionUtil.isLoginUser(session));
        User loginUser = (User)session.getAttribute("loginUser");

        if(loginUser == null) {
            return "로그인 정보가 존재하지 않습니다.";
        }
        return loginUser.getUserId();
    }

    /**
     * 나의 리뷰 조회 메서드
     * @param session
     * @return
     */
    @GetMapping("/api/mypage/reviews")
    public ResponseEntity<List<ReviewItemDTO>> getMyReviews(HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);

        if(loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ReviewItemDTO> list = myPageService.getMyReview(loginUser.getUserId());
        return ResponseEntity.ok(list);
    }

    /**
     * 보유 포인트 조회
     * @param session
     * @return
     */
    @GetMapping("/api/mypage/point")
    public UserPoint getUserPoint(HttpSession session) {
        String userId = getLoginUserId(session);
        return myPageService.getUserPoint(userId);
    }

    /**
     * 포인트 변동 내역 조회 메서드
     * @param session
     * @return
     */
    @GetMapping("api/mypage/point/detail")
    public ResponseEntity<List<UserPointHistory>> getUserPointHistory(HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);

        if(loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<UserPointHistory> list = myPageService.getUserPointHistory(loginUser.getUserId());
        return ResponseEntity.ok(list);
    }

    /**
     * 회원정보 조회 메서드
     * @param session
     * @return
     */
    @GetMapping("/api/mypage/info")
    public User getUserInfo(HttpSession session) {
        String userId = getLoginUserId(session);
        return myPageService.getUserInfo(userId);
    }

    /**
     * 비밀번호 변경 메서드
     * @param session
     * @param body JSON 형태의 비밀번호 정보 (oldPassword, newPassword, newPasswordCheck)
     */
    @PatchMapping("/api/mypage/password")
    public ResponseEntity<Map<String, String>> changePassword(HttpSession session,
                                                              @RequestBody Map<String, String> body) {
        String userId = getLoginUserId(session);
        Map<String, String> resBody = new HashMap<>();

        if(userId == null) {
            log.error("비밀번호 변경 실패-로그인되지 않음: {}", userId);
            body.put("message", "로그인 상태가 아닙니다.");
            return ResponseEntity.badRequest().body(resBody);
        }

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        // String newPasswordCheck = body.get("newPasswordCheck");

        try {
            int updated = myPageService.changePassword(userId, oldPassword, newPassword);

            if(updated == 0) {
                log.error("비밀번호 변경 실패: {}", userId);
                resBody.put("message", "비밀번호 변경에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resBody);
            }

            log.info("비밀번호 변경 완료: {} -> {}", oldPassword, newPassword);
            resBody.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            // SessionUtil.logout(session);  // 로그아웃 후 다시 로그인하도록 유도
            return ResponseEntity.ok(resBody);

        } catch(IllegalArgumentException e) {
            resBody.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(resBody);  // 400
        } catch(Exception e) {
            resBody.put("message", "서버 오류로 비밀번호 변경에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resBody);
        }
    }

    /**
     * 로그아웃 메서드
     * @param session
     */
    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        Map<String, String> body = new HashMap<>();
        Object loginUser = SessionUtil.getLoginUser(session);

        if(loginUser == null) {
            log.error("로그아웃 실패: {}", loginUser);
            body.put("message", "로그인 상태가 아닙니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        log.info("로그아웃 성공: {}", loginUser);
        SessionUtil.logout(session);
        body.put("message", "로그아웃 되었습니다.");
        return ResponseEntity.ok(body);
    }

    /**
     * 회원 탈퇴
     * @param session
     * @return
     */
    @DeleteMapping("/api/mypage/withdraw")
    public ResponseEntity<Map<String, String>> withdrawUser(HttpSession session) {
        String userId = getLoginUserId(session);
        myPageService.withdrawUser(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 탈퇴가 완료되었습니다.");
        log.info("회원탈퇴 성공: {}", userId);
        return ResponseEntity.ok(response);
    }
}
