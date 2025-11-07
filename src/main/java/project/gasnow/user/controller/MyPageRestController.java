package project.gasnow.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/mypage")
@Slf4j
public class MyPageRestController {
    private final MyPageService myPageService;

    private String getLoginUserId(HttpSession session) {
        User loginUser = (User)session.getAttribute("loginUser");
        return loginUser.getUserId();
    }

    /**
     * 나의 리뷰 조회 메서드
     * @param session
     * @return
     */
    @GetMapping("/api/mypage/reviews")
    public List<ReviewItemDTO> getMyReviews(HttpSession session) {
        String userId = getLoginUserId(session);
        return myPageService.getMyReview(userId);
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
    @GetMapping("/point/detail")
    public List<UserPointHistory> getUserPointHistory(HttpSession session) {
        String userId = getLoginUserId(session);
        return myPageService.getUserPointHistory(userId);
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
     * @param oldPassword 클라이언트가 입력한 기존 비밀번호
     * @param newPassword 클리이언트가 입력한 변경 비밀번호
     */
    @PatchMapping("/api/mypage/password")
    public void changePassword(HttpSession session, String oldPassword, String newPassword) {
        String userId = getLoginUserId(session);
        myPageService.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 로그아웃 메서드
     * @param session
     */
    @PostMapping("/api/logout")
    public void logout(HttpSession session) {
        String userId = getLoginUserId(session);
        SessionUtil.logout(session);
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
        return ResponseEntity.ok(response);
    }
}
