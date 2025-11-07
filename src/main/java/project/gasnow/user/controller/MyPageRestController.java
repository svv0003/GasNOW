package project.gasnow.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.user.model.dto.ReviewItemDTO;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;
import project.gasnow.user.model.service.MyPageService;

import java.util.List;

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
    @GetMapping("/reviews")
    public List<ReviewItemDTO> getMyReviews(HttpSession session) {
        String userId = getLoginUserId(session);
        return myPageService.getMyReview(userId);
    }

    /**
     * 보유 포인트 조회
     * @param session
     * @return
     */
    @GetMapping("/point")
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
    @GetMapping("/info")
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
    @PatchMapping("/password")
    public void changePassword(HttpSession session, String oldPassword, String newPassword) {
        String userId = getLoginUserId(session);
        myPageService.changePassword(userId, oldPassword, newPassword);
    }

    // 회원 탈퇴
}
